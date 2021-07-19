<template>
    <div>
        <b-navbar toggleable="lg" type="dark" variant="info">
        <b-navbar-brand href="/">My_Youtube</b-navbar-brand>

        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

        <b-collapse id="nav-collapse" is-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
            <b-nav-form >
            <b-form-input size="sm" class="mr-sm-2" placeholder="Search" id="search" @input="startSearching"></b-form-input>
            <b-button size="sm" class="my-2 my-sm-0" v-on:click="startSearching">Search</b-button>
            </b-nav-form>

            <b-nav-item-dropdown right>
            <!-- Using 'button-content' slot -->
            <template #button-content>
                <em>{{username}}</em>
                </template>
                <b-dropdown-item v-show="!connect" href="/login">Login</b-dropdown-item>
                <b-dropdown-item v-show="!connect" href="/register">Register</b-dropdown-item>
                <b-dropdown-item v-show="connect" href="/pannel">Pannel</b-dropdown-item>
                <b-button size="sm" class="my-2 my-sm-0 m-3 mt-3 w-75" variant="danger" v-show="connect" v-on:click="disconect">disconect</b-button>
            </b-nav-item-dropdown>
        </b-navbar-nav>
        </b-collapse>
    </b-navbar>
    </div>
</template>


<script>
export default {
    data() {
        return {
            username : "",
            connect: false
        }
    },
    mounted() {
        if (sessionStorage.getItem("username") == null) {
            this.username = "";
        }
        else {
            this.username = sessionStorage.getItem("username");
            this.connect = true;
        }
    },
    methods: {
        startSearching(e) {
            let test = {search};
            console.log(test.search.value);
        },
        disconect() {
            sessionStorage.clear();
           this.$router.push({path: "/"});
        }
    }
}
</script>