require dpkg.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI += "file://noman.patch \
            file://check_snprintf.patch \
            file://check_version.patch \
            file://preinst.patch \
            file://fix-timestamps.patch \
            file://remove-tar-no-timestamp.patch \
            file://fix-abs-redefine.patch \
           "

SRC_URI[md5sum] = "4df9319b2d17e19cdb6fe94dacee44da"
SRC_URI[sha256sum] = "73cd7fba4e54acddd645346b4bc517030b9c35938e82215d3eeb8b4e7af26b7a"

PR = "${INC_PR}.0"
