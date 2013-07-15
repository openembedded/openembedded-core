require dpkg.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI += "file://noman.patch \
            file://check_snprintf.patch \
            file://check_version.patch \
            file://preinst.patch \
            file://fix-timestamps.patch \
            file://remove-tar-no-timestamp.patch \
            file://Add-support-for-mipsn32-el-and-mips64-el-to-arch-tab.patch"

SRC_URI[md5sum] = "a20a06a5272717274a8b009368f237da"
SRC_URI[sha256sum] = "aeaacf0884039940d9463901102194f9a42eb5702157b9e7a23f43e0d9f65cf2"

PR = "${INC_PR}.0"
