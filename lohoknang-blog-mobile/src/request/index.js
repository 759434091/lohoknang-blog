import axios from 'axios'

axios.defaults.baseURL = 'https://api.lohoknang.blog/'
axios.defaults.withCredentials = true

async function getBlogs(page) {
    return await axios.get('/blogs', {
        params: {
            page: page,
            type: "raw",
            value: ""
        }
    })
}

async function getBlog(blogId) {
    return await axios.get(`/blogs/${blogId}`);
}

async function sendEmail(email) {
    return await axios.post('/email', email)
}

export default {
    getBlogs,
    getBlog,
    sendEmail
}
