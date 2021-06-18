package main

import (
	"amneos/controllers"
	"fmt"
	"github.com/gin-gonic/gin"
)

func main() {
	fmt.Println("main -> Program started")


	r := gin.Default()

	r.POST("/video", controllers.GetVideos)

	err := r.Run(":8081")
	if err != nil {
		return
	}
}
