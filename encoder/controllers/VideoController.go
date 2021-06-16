package controllers

import (
	"fmt"
	"github.com/gin-gonic/gin"
	_ "github.com/giorgisio/goav/avformat"
	"log"
	"net/http"
)

func SayVideo() {
	fmt.Println("videoController")
}

func GetVideos(c *gin.Context) {

	file, err := c.FormFile("file")
	if err != nil {
		log.Fatal(err)
	}
	log.Println(file.Filename)

	err = c.SaveUploadedFile(file, "temp/"+file.Filename)
	if err != nil {
		log.Fatal(err)
	}
	c.JSON(http.StatusOK, file.Filename)
}
