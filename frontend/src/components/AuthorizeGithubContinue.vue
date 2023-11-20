<template>
    <div style="padding: 30px; padding-top: 95px; opacity: 0.95;">
        <a-card title="Continue" :bordered="false">
            <a-form
                    name="normal_login"
                    class="login-form"
                    :label-col="{ span: 8 }"
                    :wrapper-col="{ span: 8 }"
                    autocomplete="off"
                    @finishFailed="">
                <a-form-item :wrapper-col="{ offset: 8, span: 4 }">
                    <p>Requested, wating response...</p>
                </a-form-item>
            </a-form>
        </a-card>
    </div>
</template>
<script lang="ts" name="vBxRRLaje">
    import { defineComponent, reactive } from 'vue';
    import { message } from 'ant-design-vue';
    import { HttpInstance } from '@/http/AxiosHttp';
    import { BaseURL } from '@/http/BaseURL'; 
    import { getQueryParams } from '@adso-ts/get-query-params';
    import VueCookies from 'vue-cookies';

    export default defineComponent({
        setup() {
            document.title = "Continue";

            const paramsObject = getQueryParams(decodeURIComponent(window.location.href));
            const requestBody = {
                code: paramsObject.code,
                state: paramsObject.state,
            };

            if (paramsObject.state != sessionStorage.getItem("STATE_UUID")) {
                message.error("Error authorize")
                sessionStorage.removeItem("STATE_UUID")
                return;
            }
            sessionStorage.removeItem("STATE_UUID")

            message.loading("Requesting...", 0.5);
            HttpInstance.post("/oauth/client/continue/github", requestBody)
                .then(function (response) {
                    if (response.data.SessionID) {
                        VueCookies.set('SESSIONID', response.data.SessionID, {
                            domain: 'localhost',
                            path: '/',
                            httpOnly: true,
                            secure: true
                        });

                        message.info("Success", 0.5);
                        if(sessionStorage.getItem("HISTOR_URL")) {
                            console.log(paramsObject.redirect_uri);
                            window.location.href = decodeURIComponent(sessionStorage.getItem("HISTOR_URL"));
                            sessionStorage.removeItem("HISTOR_URL");
                        } else {
                            window.location.href = BaseURL + "/"
                        }
                    }
                    else {
                        message.error(response.data.reason);
                    }
                })
            const onFinishFailed = () => {
                message.error("Unknown error");
            };
            return {
            }
        }
    })
</script>