import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import ForgotPasswordView from '../views/ForgotPasswordView.vue'
import ForgotPasswordVerifyView from '../views/ForgotPasswordVerifyView.vue'
import HomeView from '../views/HomeView.vue'
import RegisterView from '../views/RegisterView.vue'
import RegisterVerifyView from '../views/RegisterVerifyView.vue'
import AuthorizeConfirmView from '../views/AuthorizeConfirmView.vue'
import AuthorizeGithubContinueView from '../views/AuthorizeGithubContinueView.vue'
import LicenseAndStandardsView from '../views/LicenseAndStandardsView.vue'
import VueCookies from 'vue-cookies';

import { HttpInstance } from '@/http/AxiosHttp';
import { BaseURL } from '@/http/BaseURL'; 

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: BaseURL + '/login',
            name: 'login',
            component: LoginView,
            meta: {
                requireAuth: false
            }
        },
        {
            path: BaseURL + '/',
            name: 'home',
            component: HomeView,
            meta: {
                requireAuth: true
            }
        },
        {
            path: BaseURL + '/authorize/confirm',
            name: 'authorize',
            component: AuthorizeConfirmView,
            meta: {
                requireAuth: true
            }
        },
        {
            path: BaseURL + '/authorize/github/continue',
            name: 'authorize-github-continue',
            component: AuthorizeGithubContinueView,
            meta: {
                requireAuth: false
            }
        },
        {
            path: BaseURL + '/register',
            name: 'register',
            component: RegisterView,
            meta: {
                requireAuth: false
            }
        },
        {
            path: BaseURL + '/register/verify',
            name: 'register-verify',
            component: RegisterVerifyView,
            meta: {
                requireAuth: false
            }
        },
        {
            path: BaseURL + '/login/forgot',
            name: 'login-forgot',
            component: ForgotPasswordView,
            meta: {
                requireAuth: false
            }
        },
        {
            path: BaseURL + '/login/forgot/verify',
            name: 'login-forgot-verify',
            component: ForgotPasswordVerifyView,
            meta: {
                requireAuth: false
            }
        },
        {
            path: BaseURL + '/logout',
            name: 'logout',
            component: () => {
                HttpInstance.get("/oauth/un-authorize", {
                    Headers: {
                        "X-Access-Token": VueCookies.get("SESSIONID"),
                    }
                });

               VueCookies.remove("SESSIONID")
                window.location.href = BaseURL + "/login";
            },
            meta: {
                requireAuth: true
            }
        }

    ]
})

router.beforeEach(async (to, from, next) => {
    const Session = VueCookies.get("SESSIONID");
    if (to.meta.requireAuth === false) {
        if (!Session) {
            next()
        }
        else {
            next({
                path: BaseURL + '/'
            })
        }
        
    }
    else {
        if (!Session) {
            if (window.location.href !== BaseURL + "/login") {
                var param: string = encodeURIComponent(window.location.href);
                window.location.href = BaseURL + "/login?redirect_uri=" + param;
            } else {
                next({
                    path: BaseURL + "/login",
                });
            }
        }
        else {
            next()
        }
    }
});

export default router
