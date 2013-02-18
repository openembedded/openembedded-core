require shared-mime-info.inc
PR = "r0"

SRC_URI[md5sum] = "12ba00bf1cb2e69bfba73127e708e833"
SRC_URI[sha256sum] = "184d094b157a9ec2607ad26a1a1837e6e07f3fcbeb38d8b6d412906156f9e06c"

SRC_URI =+ "file://parallelmake.patch \
	    file://install-data-hook.patch"
