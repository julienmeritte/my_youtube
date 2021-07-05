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
                    <label for="username">Pseudo</label>
                    <input type="text" class="form-control" id="pseudo" v-model="pseudo" placeholder="Pseudo">
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" class="form-control" id="email" aria-describedby="emailHelp" v-model="mail" placeholder="Enter email">
                </div>
                <div class="form-group">
                    <label for="userPassword">Password</label>
                    <input type="password" class="form-control" id="password" v-model="password" placeholder="Password">
                </div>
                <button class="btn btn-primary" v-on:click="registerCall">Submit</button>
            </form>
        </div>
    </div>
    
</template>


<script>
export default {
    data() {
        return {
            userName: "",
            password: "",
            mail: "",
            pseudo: "",
        }
    },
    methods: {
            async registerCall(e) {
                    console.log(this.password + "  " + this.userName + "  " + this.pseudo + "  " + this.mail);
                    //const ip = await this.$axios.$get('api/auth?login=admin&password=admin');
                    //this.ip = ip;
                    //console.log(this.ip);
            }
        }
}
</script>