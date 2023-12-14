<template>
    <div style="padding: 30px; padding-top: 95px;">
        <a-card title="Authorization Required" :bordered="false" style="width: 100%; background: #fff; opacity: 0.95" >
            <a-form
                    name="normal_login"
                    class="login-form"
                    :label-col="{ span: 8 }"
                    :wrapper-col="{ span: 8 }"
                    autocomplete="off"
                    @finish="onFinish"
                    @finishFailed="onFinishFailed">
                <a-form-item :wrapper-col="{ offset: 8, span: 8 }">
                    <h2>Authorize</h2>
                    <template v-if="AppName">
                        <p>{{ AppName }} will get information below:</p>
                        <template v-if="Gets" v-for="t in Gets">
                            <li style="list-style-type: disc">{{ t }}</li>
                        </template>
                        <p>Click confirm and app will be authorized.</p>
                        <a-space>
                            <a-button type="primary" html-type="submit" class="login-form-button" @click="onFinish">
                                Confirm
                            </a-button>
                        </a-space>
                    </template>
                    <template v-if="!AppName">
                        <p>404 Not found</p>
                        <!--
                        <a-space>
                            <a-button type="primary" html-type="submit" class="login-form-button" @click="onFinish">
                                Confirm
                            </a-button>
                        </a-space>
                        -->
                    </template>
                </a-form-item>
            </a-form>
        </a-card>
    </div>
</template>
<script lang="ts">
    import { defineComponent, reactive, ref } from 'vue';
    import { message } from 'ant-design-vue';
    import VueCookies from 'vue-cookies';
    import { HttpInstance } from '@/http/AxiosHttp';
    import { getQueryParams } from '@adso-ts/get-query-params';
    export default defineComponent({
        setup() {
            document.title = "Authorization Required";
            
            const paramsObject = getQueryParams(decodeURIComponent(window.location.href));

            const AppName = ref('');
            const Gets = ref('');

            HttpInstance.post("/oauth/info/" + paramsObject.client_id, {
                scope: paramsObject.scope
            }, 
            {
                headers: {
                    "X-Access-Token": VueCookies.get("SESSIONID"),
                }
            })
                .then((response) => {
                    console.log(response.data);
                    AppName.value = response.data.clientName;
                    Gets.value = response.data.clientGets;
                })
                .catch((error) => {
                })

            const onFinish = () => {
                message.loading("Requesting...", 0.5);

                const authorizeRequestBody = {
                    response_type: paramsObject.response_type,
                    client_id: paramsObject.client_id,
                    scope: paramsObject.scope,
                    state: paramsObject.state,
                    redirect_uri: paramsObject.redirect_uri,
                };

                HttpInstance.post("/oauth/authorize", authorizeRequestBody, {
                    headers: {
                        "X-Access-Token": VueCookies.get("SESSIONID"),
                    }
                })
                    .then((response) => {
                        if (response.data.statusCode == 401) {
                            message.error(response.data.reason);
                        } else {
                            const redirectURI = response.data.url;
                            const code = response.data.code;
                            const state = response.data.state;
                            if(redirectURI == undefined) {
                                message.error("Can not authorize");
                                return;
                            }
                            window.location.href = redirectURI + "?code=" + code + "&state=" + state;
                        }
                    })
                    .catch((error) => {
                        message.error("Session expired");
                        window.location.href = "/logout";
                    })
            };
            const onFinishFailed = () => {
                message.error("Unknown error");
            };
            return {
                onFinish,
                onFinishFailed,
                AppName,
                Gets
            }
        }
    })
</script>