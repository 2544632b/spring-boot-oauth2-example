<template>
    <div class="sign-in">
        <a-card style="opacity: 0.95;">
            <a-row type="flex" :gutter="[24,24]" justify="space-around" align="middle">
                <!-- Sign In Form Column -->
                <a-col :span="24" :md="12" :lg="{span: 12, offset: 0}" :xl="{span: 6, offset: 2}" class="col-form">
                    <h1 class="mb-15">Sign in</h1>
                    <h5 class="font-regular text-muted"></h5>

                    <!-- Sign In Form -->
                    <a-form
                        :model="formState"
                        name="normal_login"
                        class="login-form"
                        autocomplete="off"
                        @finish="onFinish"
                        @finishFailed="onFinishFailed"
                    >
                        <a-form-item class="mb-10" style="display: block; margin-bottom: 10px;" label="Username or Email" :colon="false" name="keywords" :rules="[{ required: true, message: 'Enter an username or email' }]">
                            <a-input v-model:value="formState.keywords" />
                        </a-form-item>
                        <a-form-item class="mb-5" style="display: block; margin-bottom: 10px;" label="Password" :colon="false" name="password" :rules="[{ required: true, message: 'Enter a password' }]">
                            <a-input v-model:value="formState.password" type="password" />
                        </a-form-item>
                        <a-form-item class="mb-10" style="display: block; margin-top: 25px; margin-bottom: 30px;">
                            <a-button type="primary" html-type="submit" block class="login-form-button">
                                Sign in
                            </a-button>
                        </a-form-item>
                    </a-form>
                    <!-- / Sign In Form -->
                    <p class="font-semibold text-muted"><router-link to="/register" class="font-bold text-dark">Sign up</router-link></p>
                    <p class="font-semiblod text-muted"><a :href="githubURL" class="font-blod text-dark">Sign in from GitHub</a></p>
                    <p class="font-semiblod text-muted"><router-link to="/login/forgot" class="font-blod text-dark">Forgot password</router-link></p>
                </a-col>
                <!-- / Sign In Form Column -->

                <!-- Sign In Image Column -->
                <a-col :span="24" :md="12" :lg="12" :xl="12" class="col-img">
                    <img src="/login-poi/kongou.png" alt="kongou">
                </a-col>
                <!-- Sign In Image Column -->
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

.sign-in {
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
<script lang="ts" name="c0JBaXj">
    import { defineComponent, reactive } from 'vue';
    import { HttpInstance } from '@/http/AxiosHttp';
    import { BaseURL } from '@/http/BaseURL'; 
    import { message } from 'ant-design-vue';
    import VueCookies from 'vue-cookies';
    import { getQueryParams } from '@adso-ts/get-query-params';

    interface FormState {
        keywords: string;
        password: string;
    }
    
    export default defineComponent({
        setup() {
            const uuid = self.crypto.randomUUID();
            sessionStorage.setItem("STATE_UUID", uuid);
            const githubURL = "https://github.com/login/oauth/authorize?client_id=CLIENT_ID&redirect_uri=http://localhost/authorize/github/continue&scope=user&state=" + uuid + "&response_type=code";

            const paramsObject = getQueryParams(window.location.href);
            if(paramsObject.redirect_uri) {
                sessionStorage.setItem("HISTOR_URL", paramsObject.redirect_uri);
            }

            const formState = reactive<FormState>({
                keywords: '',
                password: '',
            });

            const onFinish = (values: any) => {
                message.loading("Requesting...", 0.5)
                HttpInstance.post("/login", values, {
                    headers: {
                        "Accept": "application/json",
                    }
                })
                    .then(function (response) {
                        if (response.data.SessionID) {
                            VueCookies.set('SESSIONID', response.data.SessionID, {
                                domain: 'localhost',
                                path: '/login',
                                httpOnly: true,
                                secure: true
                            });

                            message.info("Success");
                            if(sessionStorage.getItem("HISTOR_URL")) {
                                console.log(paramsObject.redirect_uri);
                                window.location.href = decodeURIComponent(sessionStorage.getItem("HISTOR_URL"));
                                sessionStorage.removeItem("HISTOR_URL");
                            } else {
                                window.location.href = BaseURL + "/"
                            }
                        }
                        else {
                            console.log(response.data);
                            message.error(response.data.reason);
                        }
                    })
            };

            const onFinishFailed = (errorInfo: any) => {
                message.warn("Invalid form");
            };

            return {
                formState,
                onFinish,
                onFinishFailed,
                githubURL
            };
        },
    });
</script>