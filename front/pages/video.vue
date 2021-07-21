<template>
    <div>
        <div>
            <Header />
        </div>
        <div class="container-fluid">
            <div class="row d-flex flex-row">
                <div class="col-9 bg-info bg-white">
                    <br>
                    <div class="embed-responsive embed-responsive-16by9">
                        <iframe loading="lazy" class="embed-responsive-item" :src="`${currentVideo}`"  allowfullscreen></iframe>
                    </div>
                    <br>
                    <div class="d-flex-inline">
                        <p class="font-weight-bold h3">{{tittleVideo}}</p>
                        <button type="button" v-show="this.typeQuality[0]" v-on:click="quality('240')" class="btn btn-secondary w-15 h-50">240</button>
                        <button type="button" v-show="this.typeQuality[1]" v-on:click="quality('360')" class="btn btn-secondary w-15 h-50">360</button>
                        <button type="button" v-show="this.typeQuality[2]" v-on:click="quality('480')" class="btn btn-secondary w-15 h-50">480</button>
                        <button type="button" v-show="this.typeQuality[3]" v-on:click="quality('720')" class="btn btn-secondary w-15 h-50">720</button>
                        <button type="button" v-show="this.typeQuality[4]" v-on:click="quality('1080')" class="btn btn-secondary w-15 h-50">1080</button>
                    </div>
                    <br>
                        <div class="row d-flex justify-content-center">
                            <div class="col-md-11">
                                <div class="headings d-flex justify-content-between align-items-center mb-3">
                                    <h5>comments</h5>
                                </div>
                                <div class="card p-3 mt-2">
                                        <form onsubmit="return false">
                                            <div class="form-group">
                                                <input type="textarea" v-model="commentText" class="form-control" id="comment" placeholder="Enter your comment">   
                                            </div>
                                            <button class="btn btn-primary" v-on:click="createComment()">Submit</button>
                                        </form>
                                </div>
                                <div class="card p-3 mt-2" v-for="comments in commentList" :key="comments.id">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="user d-flex flex-row align-items-center"><span><p class="font-weight-bold text-primary">{{comments.username}}</p><p class="font-weight-bold">{{comments.comments}}</p></span> </div>
                                    </div>
                                </div>
                                
                            </div>
                        </div>
                </div>
                <div class="col-2 bg-info w-100 bg-white border-left border-dark">
                    <div class="m-3 d-flex flex-row" v-for="items in videoList" :key="items.videoId">
                        <img class="w-100 h-100 p-2 img-fluid img-thumbnail border-0" :src="`${items.videoImg}`">
                        <p class="p-2" v-on:click="videoRedirect(items.videoLink)" >{{items.videoName}}</p>
                        
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
            apiUrl : process.env.apiUrl,
            commentText: "",
            currentVideo: "",
            tittleVideo: "",
            typeQuality: [],
            videoInfo: [],
            videoList: [] , 
            commentList: [],
        }
    },
    async mounted() {
        let idVideo = sessionStorage.getItem("idVideo");
        let idUser = sessionStorage.getItem("id");
        let tokenUser = sessionStorage.getItem("token");
        let quality = sessionStorage.getItem("quality");
        try {
            const response = await this.$axios.$get(this.apiUrl + '/videos?perPage=100');
            let json = [];
            response.data.forEach(element => {
                json["videoName"] = element.name;
                json["videoImg"] = this.apiUrl + '/videos/base' + element.name + '.mp4';
                json["videoLink"] = element.id;
                this.videoList.push(json);
                json = [];
            });
            response.data.forEach(element => {
                if (element.id == idVideo) {
                    this.tittleVideo = element.name.replace('.mp4' , '');
                    if (quality == null) {
                        this.currentVideo = this.apiUrl + '/videos/base' + element.name + '.mp4';
                    }
                    else {
                        this.currentVideo = this.apiUrl + '/videos/'+ quality + element.name + '.mp4';
                    }
                    this.videoInfo.push(element);
                    if (element.format ["240"] != null)
                        this.typeQuality.push(true);
                    else
                        this.typeQuality.push(false);
                    if (element.format ["360"] != null)
                        this.typeQuality.push(true);
                    else
                        this.typeQuality.push(false);
                    if (element.format ["480"] != null)
                        this.typeQuality.push(true);
                    else
                        this.typeQuality.push(false);
                    if (element.format ["720"] != null)
                        this.typeQuality.push(true);
                    else
                        this.typeQuality.push(false);
                    if (element.format ["1080"] != null)
                        this.typeQuality.push(true);
                    else
                        this.typeQuality.push(false);
                }
            });
        } catch (error) {
            console.log(error);
        }
        let json = [];
        try {
                const formData = new FormData();
                const response = await this.$axios.$get(this.apiUrl + '/video/' + idVideo + '/comments?page=1&perPage=100', {
                headers: {
                    Authorization: "Bearer " + tokenUser
                }});
                response.data.forEach(element => {
                    json ["comments"] = element.body;
                    json ["username"] = element.user.username;
                    json ["id"] = element.id;
                    this.commentList.push(json);
                    json = [];
                });
        } catch (error) {
            console.log(error);
        }   
        
    },
    methods: {
        quality(string) {
            if (string == "240")
                this.currentVideo = this.apiUrl + '/' + this.videoInfo ["0"].format ["240"];
            if (string == "360")
                this.currentVideo = this.apiUrl + '/' + this.videoInfo ["0"].format ["360"];
            if (string == "480")
                this.currentVideo = this.apiUrl + '/' + this.videoInfo ["0"].format ["480"];
            if (string == "720")
                this.currentVideo = this.apiUrl + '/' + this.videoInfo ["0"].format ["720"];
            if (string == "1080")
                this.currentVideo = this.apiUrl + '/' + this.videoInfo ["0"].format ["1080"];
        },
        videoRedirect(id) {
            sessionStorage.setItem("idVideo" , id);
            location.reload();
        },
        async createComment() {
            let idUser = sessionStorage.getItem("id");
            let tokenUser = sessionStorage.getItem("token");
            let idVideo = sessionStorage.getItem("idVideo");
            let json = [];
            try {
                const formData = new FormData();
                formData.append("body", this.commentText);
                const response = await this.$axios.$post(this.apiUrl + '/video/' + idVideo + '/comment', formData, {
                headers: {
                    Authorization: "Bearer " + tokenUser
                }});
                json ["comments"] = response.data.body;
                json ["username"] = response.data.user.username;
                json ["id"] = response.data.id;
                this.commentList.push(json);
            } catch (error) {
                console.log(error);
            }   
        }
    },
    


}
</script>