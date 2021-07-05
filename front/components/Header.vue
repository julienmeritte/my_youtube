<template>
    <div>
        <b-navbar toggleable="lg" type="dark" variant="info">
        <b-navbar-brand href="/">My_Youtube</b-navbar-brand>

        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

        <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
            <b-nav-item href="/video">All Video</b-nav-item>
        </b-navbar-nav>

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
                <b-dropdown-item href="/account">Profile</b-dropdown-item>
                <b-dropdown-item href="/login">Login</b-dropdown-item>
                <b-dropdown-item href="/register">Register</b-dropdown-item>
                <b-dropdown-item href="/pannel">Pannel</b-dropdown-item>
                <b-dropdown-item href="#">Disconect</b-dropdown-item>
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
            username : "."
        }
    },
    mounted() {
        if (sessionStorage.getItem("username") == 'undefined') {
            this.username = "User";
        }
        else {
            this.username = sessionStorage.getItem("username");
        }
    },
    methods: {
        startSearching(e) {
            let test = {search};
            console.log(test.search.value);
        }
    }
}
</script>