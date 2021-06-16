package main

import (
	"amneos/controllers"
	"amneos/services"
	"fmt"
	"github.com/gin-gonic/gin"
)

func main() {
	fmt.Println("test")


	r := gin.Default()

	r.POST("/video", controllers.GetVideos)

	err := r.Run(":8081")
	if err != nil {
		return
	}

	controllers.SayVideo()
	services.SayEncoder()
}
