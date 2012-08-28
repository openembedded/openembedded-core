require dpkg.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI += "file://noman.patch \
            file://check_snprintf.patch \
            file://check_version.patch \
            file://preinst.patch \
            file://fix-timestamps.patch \
            file://remove-tar-no-timestamp.patch"

SRC_URI[md5sum] = "9f8042561ecccd5124e5958a1e181de7"
SRC_URI[sha256sum] = "4a1f4611390d93f1f198d910d3a4e4913b3cf81702b31f585a1872ca98df0eaa"

PR = "${INC_PR}.0"

