SUMMARY = "Dependency management tool for Golang"
HOMEPAGE = "https://github.com/golang/dep"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=1bad315647751fab0007812f70d42c0d"

GO_IMPORT = "github.com/golang/dep"
SRC_URI = "git://${GO_IMPORT} \
           file://0001-use-a-smaller-constant-that-hopefully-works-the-same.patch;patchdir=src/github.com/golang/dep \
           file://0001-Add-support-for-mips-mips64.patch;patchdir=src/github.com/golang/dep \
          "

# Points to 0.3.1 tag
SRCREV = "83789e236d7ff64c82ee8392005455fc1ec1983b"

inherit go

GO_INSTALL = "${GO_IMPORT}/cmd/dep"

RDEPENDS_${PN}-dev += "bash"
