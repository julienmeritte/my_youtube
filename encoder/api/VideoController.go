package api

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"net/http"
)

func SayVideo() {
	fmt.Println("videoController")
}

func GetVideos(c *gin.Context) {

	c.JSON(http.StatusOK, gin.H{"data": "ok"})
}