<template>
    <div>
        <div>
            <Header />
        </div>
        <div class="container-fluid">
            <div class="row">
                <div class="col-9 bg-info bg-white">
                    <br>
                    <div class="embed-responsive embed-responsive-16by9">
                        <iframe loading="lazy" class="embed-responsive-item" :src="`${currentVideo}`"  allowfullscreen></iframe>
                    </div>
                    <br>
                    <p class="font-weight-bold h3">{{tittleVideo}}</p>
                </div>
                <div class="col-3 bg-info">
                    <p>test</p>
                </div>
                
                <div class="col-9 bg-info bg-white">
                    <p>test</p>
                </div>

            </div>
        </div>
    </div>
</template>




<script>

export default {
    data() {
        return {
            currentVideo: "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            tittleVideo: "video test",
            videoList: [] , 
            commentList: [],
        }
    },
    mounted() {
        for (let index = 0; index < 15; index++) {
            this.videoList.push("https://img.youtube.com/vi/Uppcux7dJPc/0.jpg");
            this.commentList.push("cette vidéo et pas mal du tous");
        }
    },
    


}
</script>