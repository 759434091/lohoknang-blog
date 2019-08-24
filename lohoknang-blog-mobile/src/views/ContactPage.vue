<template>
    <mu-container class="cp-container">
        <h5 class="cp-brand">联系我</h5>
        <mu-form ref="contactForm"
                 :model="contact">
            <mu-form-item label="昵称" prop="name" :rules="rules.name">
                <mu-text-field :disabled="true" v-model="contact.name" prop="name"></mu-text-field>
            </mu-form-item>
            <mu-form-item label="邮箱" prop="email" :rules="rules.email">
                <mu-text-field :disabled="true" v-model="contact.email" prop="email"></mu-text-field>
            </mu-form-item>
            <mu-form-item label="联系内容" prop="content" :rules="rules.content">
                <mu-text-field :disabled="true" v-model="contact.content" prop="content"></mu-text-field>
            </mu-form-item>
            <mu-form-item>
                <mu-button v-loading="loading"
                           data-mu-loading-size="24"
                           :disabled="true"
                           :color="success"
                           v-text="buttonContent"
                           class="cp-submit"
                           @click="submit"></mu-button>
            </mu-form-item>
        </mu-form>
    </mu-container>
</template>

<script>
    export default {
        name: 'ContactPage',
        data() {
            return {
                loading: false,
                buttonContent: '留言提交',
                success: 'primary',
                disabled: false,
                contact: {
                    name: '',
                    email: '',
                    title: '',
                    content: ''
                },
                rules: {
                    name: [{validate: (val) => !!val, message: '必须填写称呼'}],
                    email: [{validate: (val) => !!val, message: '必须填写邮箱'},
                        {validate: (val) => val.length >= 3, message: '邮箱长度大于3'}],
                    content: [
                        {validate: (val) => !!val, message: '必须填写内容'},
                        {validate: (val) => val.length >= 15 && val.length <= 200, message: '内容长度大于15小于200'}
                    ]
                }
            }
        },
        methods: {
            submit() {
                if (this.disabled)
                    return;
                this.$refs.contactForm
                    .validate()
                    .then((valid) => {
                        if (!valid) {
                            return
                        }
                        this.loading = true;
                        this.$request.sendEmail(this.contact)
                            .then((res) => {
                                if (res.data.response.includes('250 Ok')) {
                                    this.success = 'success'
                                    this.buttonContent = '成功'
                                    this.disabled = true
                                } else {
                                    this.success = 'error'
                                    this.buttonContent = '失败'
                                    this.disabled = true
                                }
                            })
                            .catch(() => {
                                this.success = 'error'
                                this.buttonContent = '失败'
                                this.disabled = true
                            })
                            .finally(() => this.loading = false)
                    })
            }
        }
    }
</script>

<style scoped>
    .cp-container {
        padding: 1rem 2rem 2rem 2rem;
    }

    .cp-brand {
        font-size: 24px;
        font-weight: 400;
    }

    .cp-submit {
        margin-left: 0;
    }
</style>
