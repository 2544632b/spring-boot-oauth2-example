<template>
    <div class="user-profile">
        <a-card style="opacity: 0.95;">
            <a-row type="flex" :gutter="[24,24]" justify="space-around" align="middle">
                <a-col :span="24" :md="12" :lg="{span: 12, offset: 0}" :xl="{span: 6, offset: 2}" style="text-align: center">
                    <template v-if="email">
                        <img class="avatar" :src="'https://gravatar.loli.net/avatar/' + email" :alt="email" width="80" height="80">
                    </template>
                    <h2>{{ username }}</h2>
                    <p v-if="email || username">{{ email }}</p>
                    <p>Last login: {{ login }}</p>
                    <router-link :to="BaseURL + '/logout'">Sign out</router-link>
                </a-col>
                <a-col :span="24" :md="12" :lg="12" :xl="12">
                    <a-form :model="formState"
                        name="normal_login"
                        class="login-form"
                        autocomplete="off"
                        @finish="onFinish"
                        @finishFailed="onFinishFailed" style="padding-top: 30px;">
                        <h3>Change password</h3>
                        <a-form-item 
                                    name="password"
                                    :rules="[{ required: true, message: 'Please enter a password' }]">
                            <a-input v-model:value="formState.password" type="password" placeholder="Enter password...">
                            </a-input>
                        </a-form-item>
                        <a-form-item
                                    name="new_password"
                                    :rules="[{ required: true, message: 'Please enter a new password' }]">
                            <a-input v-model:value="formState.new_password" type="password" placeholder="Enter new password...">
                            </a-input>
                        </a-form-item>
                        <a-form-item style="text-align: center">
                            <a-space>
                                <a-button type="primary" html-type="submit" class="login-form-button">
                                    Change password
                                </a-button>
                            </a-space>
                        </a-form-item>
                    </a-form>
                </a-col>
            </a-row>
        </a-card>
    </div>
</template>
<style>
    .user-profile {
        padding-top: 95px;
        padding-left: 100px;
        padding-right: 100px;
        @media screen and (max-width: 768px) {
            padding-left: 30px;
            padding-right: 30px;
        }
    }
    .avatar {
        border-radius: 50%;
        box-shadow: 1px 1px 15px rgba(0,0,0,0.1);
        @media screen and (max-width: 768px) {
            display: none;
        }
    }
</style>
<script lang="ts" name="BQ8vuTjSy">
    import { defineComponent, reactive, ref } from 'vue';
    import { HttpInstance } from '@/http/AxiosHttp';
    import { BaseURL } from '@/http/BaseURL'; 
    import { message } from 'ant-design-vue';
    import VueCookies from 'vue-cookies';

    interface FormState {
        password: string;
        new_password: string;
    }
    export default defineComponent({
        setup() {
            console.log(document.referrer);

            const formState = reactive<FormState>({
                password: '',
                newPassword: ''
            });

            const username = ref('');
            const email = ref('');
            const login = ref('');

            HttpInstance.get("/user/info", {
                headers: {
                    "X-Access-Token": VueCookies.get("SESSIONID"),
                }
            })
                .then((response) => {
                    console.log(response.data)
                    username.value = response.data.username;
                    email.value = response.data.email;
                    login.value = response.data.lastLoginDate;
                })
                .catch((error) => {
                    message.error(error.response.data.reason)
                    window.location.href = BaseURL + "/logout"
                })

            const onFinish = (values: any) => {
                const msg = message.loading("Requesting...", 0.5)
                HttpInstance.post("/user/change/password", values, {
                    headers: {
                        "X-Access-Token": VueCookies.get("SESSIONID"),
                    }
                })
                    .then((response) => {
                        if (response.data.statusCode == 200) {
                            VueCookies.remove("SESSIONID")
                            message.info(response.data.reason);
                            window.location.href = BaseURL + "/login"
                        } else {
                            message.error(response.data.reason)
                        }
                    })
                    .catch((error) => {
                        message.error(error.response.data.reason);
                        window.location.href = BaseURL + "/logout";
                    })
            };

            const onFinishFailed = (errorInfo: any) => {
                message.warn("Invalid form");
            };
            return {
                formState,
                onFinish,
                onFinishFailed,
                username,
                email,
                login,
                BaseURL
            };
        },
    });
</script>