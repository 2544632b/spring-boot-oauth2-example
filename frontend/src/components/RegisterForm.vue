<template>
    <div class="sign-up">
        <a-card style="opacity: 0.95;">
            <a-row type="flex" :gutter="[24,24]" justify="space-around" align="middle">
                <a-col :span="24" :md="12" :lg="{span: 12, offset: 0}" :xl="{span: 6, offset: 2}" class="col-form">
                    <h1 class="mb-15">Register</h1>
                    <h5 class="font-regular text-muted"></h5>
                    <a-form
                        :model="formState"
                        name="normal_login"
                        class="login-form"
                        autocomplete="off"
                        @finish="onFinish"
                        @finishFailed="onFinishFailed"
                    >
                        <a-form-item label="Email"
                                    name="email"
                                    class="mb-10" style="display: block; margin-bottom: 5px;"
                                    :colon="false"
                                    :rules="[{ required: true, type: 'email', message: 'Invalid email' }]">
                            <a-input v-model:value="formState.email">
                            </a-input>
                        </a-form-item>
                        <a-form-item label="Username"
                                    name="username"
                                    class="mb-10" style="display: block; margin-bottom: 5px;"
                                    :colon="false"
                                    :rules="[{ required: true, message: 'Username could not be blank' }]">
                            <a-input v-model:value="formState.username">
                            </a-input>
                        </a-form-item>
                        <a-form-item label="Password"
                                    name="password"
                                    class="mb-5" style="display: block; margin-bottom: 5px;"
                                    :colon="false"
                                    :rules="[{ required: true, message: 'Password could not be blank' }]">
                            <a-input v-model:value="formState.password" type="password">
                            </a-input>
                        </a-form-item>
                        <a-form-item label="Confirm password"
                                    name="repeatPassword"
                                    class="mb-5" style="display: block; margin-bottom: 5px;"
                                    :colon="false"
                                    :rules="[{ required: true, message: 'Different password', validator: validatePass, trigger: 'change' }]">
                            <a-input v-model:value="formState.repeatPassword" type="password">
                            </a-input>
                        </a-form-item>
                        <a-form-item class="mb-10" style="display: block; margin-top: 25px; margin-bottom: 30px;">
                            <a-button type="primary" html-type="submit" block class="login-form-button">
                                Register
                            </a-button>
                        </a-form-item>
                    </a-form>
                    <p class="font-semibold text-muted"><router-link :to="BaseURL + '/login'" class="font-bold text-dark">Sign in</router-link></p>
                    <p class="font-semiblod text-muted"><a :href="oauthURL" class="font-blod text-dark">Sign from GitHub</a></p>
                    <p class="font-semiblod text-muted"><router-link :to="BaseURL + '/login/forgot'" class="font-blod text-dark">Forgot password</router-link></p>
                </a-col>
                <a-col :span="24" :md="12" :lg="12" :xl="12" class="col-img">
                    <img src="/login-poi/kongou.png" alt="kongou">
                </a-col>
            </a-row>
        </a-card>
    </div>
</template>
<style>
    .layout-dashboard-rtl {
        .header-control {
            .header-search {
                .ant-input {
                    &:not(:first-child) {
                        padding-right: 15px;
                        padding-left: 11px;
                    }
                }
            }
        }

        .ant-input-affix-wrapper .ant-input-prefix {
            right: 12px;
            left: auto;
        }
    }

    .sign-up {
        padding-top: 95px;
        padding-left: 100px;
        padding-right: 100px;
        @media(max-width: 992px) {
            padding-left: 50px;
            padding-right: 50px;
            padding-top: 95px;
        }
        .col-img img {
            width: 100%;
            max-width: 500px;
            margin: auto;
            display: block;

            @media( min-width: 992px) {
                margin: 0;
            }
        }
        .col-form .ant-switch {
            padding-top: 15px;
            margin-right: 5px;
        }
        .ant-form-item-label {
            display: inline;
            line-height: 28px;
        }
        .ant-form-item label {
            font-size: 13px;
            font-weight: 600;
            color: #141414;
        }
    }

    @media(max-width: 768px) {
        img {
            display: none!important;
        }
    }
</style>
<script lang="ts" name="Xjp89F7JXQL!lyx">
    import { defineComponent, reactive, ref } from 'vue';
    import { HttpInstance } from '@/http/AxiosHttp';
    import { BaseURL } from '@/http/BaseURL'; 
    import { message } from 'ant-design-vue';
    import type { Rule } from 'ant-design-vue/es/form';
    import type { FormInstance } from 'ant-design-vue';

    interface FormState {
        email: string;
        username: string;
        password: string;
        repeatPassword: string;
    }
    export default defineComponent({
        setup() {
            const uuid = self.crypto.randomUUID();
            sessionStorage.setItem("STATE_UUID", uuid);
            const oauthURL = "https://github.com/login/oauth/authorize?client_id=000000&redirect_uri=http://localhost/authorize/github/continue&scope=user&state=" + uuid + "&response_type=code";

            const formState = reactive<FormState>({
                email: '',
                username: '',
                password: '',
                repeatPassword: '',
            });

            let validatePass = async () => {
                if (formState.password !== formState.repeatPassword) {
                    return Promise.reject("Different password")
                } else {
                    return Promise.reslove;
                }
            };

            const onFinish = (values: any) => {
                const msg = message.loading("Requesting...", 0.5)
                HttpInstance.post("/register", values)
                    .then((response) => {
                        if (response.data.statusCode == 200) {
                            message.info(response.data.reason)
                            window.location.href = BaseURL + "/register/verify";
                        }
                        else {
                            message.error(response.data.reason);
                        }
                    })
                    .catch((error) => {
                        message.warn("Please try later");
                    })
            };

            const onFinishFailed = (errorInfo: any) => {
                message.warn("Invalid form");
            };
            return {
                formState,
                onFinish,
                onFinishFailed,
                validatePass,
                oauthURL,
                BaseURL
            };
        },
    });
</script>