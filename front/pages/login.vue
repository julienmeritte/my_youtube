<template>
    <div>
        <div>
            <Header />
        </div>
        <br>
        <br>
        <br>
        <br>
        <div class="container container-table">
            <form class="col-xs-4 col-xs-offset-4" onsubmit="return false">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" class="form-control" id="user" v-model="userName" placeholder="Username">
                </div>
                <div class="form-group">
                    <label for="userPassword">Password</label>
                    <input type="password" class="form-control" id="password" v-model="password" placeholder="Password">
                </div>
                <button class="btn btn-primary" v-on:click="loginCall">Submit</button>
            </form>
        </div>
    </div>

</template>


<script>
export default {
    data() {
        return {
            userName: "",
            password: ""
        }
    },
    methods: {
            async loginCall(e) {
                    try {
                        const response = await this.$axios.$post('http://localhost:8080/auth?login=' + this.userName + '&password=' + this.password);
                        this.response = response;
                        sessionStorage.setItem("username" , this.response ["data"]["user"]["username"]);
                        sessionStorage.setItem("id" , this.response ["data"]["user"]["id"]);
                        sessionStorage.setItem("token" , this.response ["data"]["token"]);
                        this.$router.push({path: "/"})
                    } catch (error) {
                        alert("invalid credentials");
                        location.reload();
                    }
            }
        }
}
</script>
