DESCRIPTION = "This is a simple example recipe that cross-compiles a Go program."
SECTION = "examples"
HOMEPAGE = "https://golang.org/"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://go.googlesource.com/example;branch=master;protocol=https"
SRCREV = "787a929d5a0dfb8bbfcdd2c4a62e0fd89466113f"
UPSTREAM_CHECK_COMMITS = "1"

GO_IMPORT = "golang.org/x/example"
GO_INSTALL = "${GO_IMPORT}/hello"

export GO111MODULE="off"

inherit go

# This is just to make clear where this example is
do_install:append() {
    mv ${D}${bindir}/hello ${D}${bindir}/${BPN}
}
