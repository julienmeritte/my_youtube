package main

import (
	"fmt"
	"amneos/encoder"
	"amneos/api"
	"github.com/gin-gonic/gin"
)

func main() {
	fmt.Println("test")


	r := gin.Default()

	r.GET("/videos", api.GetVideos)

	r.Run()

	api.SayVideo()
	encoder.SayEncoder()
}
