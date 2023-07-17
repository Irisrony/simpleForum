import {createRouter,createWebHashHistory} from 'vue-router'
import Home from '../components/Home.vue'
import Page from '../components/Page.vue'

const routes = [
    {
        path: '/',
        redirect: '/home',
    },
    {
        path: '/home',
        component: Home
    },
    {
        path: '/page',
        component: Page
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
