<template>
    <div>
        <div>
            <Header />
        </div>
        <div>
            <br>
            <br>
            <br>
            <br>
            <div class="container-fluid container-table">
                <div class="row d-flex flex-row">
                    <div class="col-8 bg-info bg-blue">
                        <div>
                            <p>test</p>
                        </div>
                    </div>
                    <div class="col-4 bg-info w-100 bg-white border-left">
                        <form onsubmit="return false">
                            <div class="form-group">
                                <label for="username">Update account</label>
                            </div>
                            <div class="form-group">
                                <label for="username">New Username</label>
                                <input type="text" class="form-control" id="user" v-model="userName" placeholder="Username">
                            </div>
                            <div class="form-group">
                                <label for="username">New Pseudo</label>
                                <input type="text" class="form-control" id="pseudo" v-model="pseudo" placeholder="Pseudo">
                            </div>
                            <div class="form-group">
                                <label for="email">New Email</label>
                                <input type="email" class="form-control" id="email" aria-describedby="emailHelp" v-model="mail" placeholder="Enter email">
                            </div>
                            <div class="form-group">
                                <label for="userPassword">New Password</label>
                                <input type="password" class="form-control" id="password" v-model="password" placeholder="Password">
                            </div>
                            <button class="btn btn-primary">Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            pseudo : "",
            password: "",
            mail: "",
            userName: "",
        }
    },
    async mounted() {
        let idUser = sessionStorage.getItem("id");
        let tokenUser = sessionStorage.getItem("token");
         const config = {
            headers: { 
                "Authorization": 'Bearer ' + tokenUser 
                }
        };
        var bodyParameters = {
            key: "value"
        };
        console.log(config);
        try {
            const response =  await this.$axios.$get('http://localhost:8080/user/' + idUser, bodyParameters , config);
            console.log("test : " +   response);
        } catch (error) {
            console.log(error);
        }
        
    },
    methods: {
        
    }
}
</script>