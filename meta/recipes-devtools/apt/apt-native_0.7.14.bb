require apt-native.inc

PR = "r7"

SRC_URI += "file://nodoc.patch \
            file://noconfigure.patch \
	    file://no-curl.patch \
	    file://includes-fix.patch"

SRC_URI[md5sum] = "19efa18fb1ef20c58b9b44e94258b814"
SRC_URI[sha256sum] = "8fc06effaf8a4e4333308eedcdc6840f1c8056f2e924210f151dfc076bcd4045"

LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
