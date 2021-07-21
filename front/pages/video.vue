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
                    <p class="font-weight-bold h3">{{tittleVideo}}</p>
                    <br>
                        <div class="row d-flex justify-content-center">
                            <div class="col-md-11">
                                <div class="headings d-flex justify-content-between align-items-center mb-3">
                                    <h5>comments</h5>
                                </div>
                                <div class="card p-3 mt-2">
                                        <form onsubmit="return false">
                                            <div class="form-group">
                                                <input type="textarea" class="form-control" id="comment" placeholder="Enter your comment">   
                                            </div>
                                            <button v-on:click="submitComment" class="btn btn-primary">Submit</button>
                                        </form>
                                </div>
                                <div class="card p-3 mt-2" v-for="comments in commentList" :key="comments.comments">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="user d-flex flex-row align-items-center"><span><p class="font-weight-bold text-primary">{{comments.username}}</p><p class="font-weight-bold">{{comments.comments}}</p></span> </div>
                                    </div>
                                </div>
                                
                            </div>
                        </div>
                </div>
                <div class="col-2 bg-info w-100 bg-white border-left border-dark">
                    <div class="m-3 d-flex flex-row" v-for="items in videoList" :key="items.videoName">
                        <img class="w-100 h-100 p-2 img-fluid img-thumbnail border-0" :src="`${items.videoLink}`">
                        <p class="p-2">{{items.videoName}}</p>
                        
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
            currentVideo: "http://localhost:8080/videos/baseTest.mp4",
            tittleVideo: "video test",
            videoList: [] , 
            commentList: [],
        }
    },
    mounted() {
        let json = [];
        let json2 = [];
        for (let index = 0; index < 15; index++) {
            json["videoName"] = "test";
            json["videoLink"] = "https://img.youtube.com/vi/Uppcux7dJPc/0.jpg"
            this.videoList.push(json);
            json2["username"] = "John doe";
            json2["comments"] = "cette vidéo et pas mal du tous";
            this.commentList.push(json2);
        }
        console.log(this.videoList);
    },
    methods: {
        submitComment() {
            console.log("test");
        }
    },
    


}
</script>