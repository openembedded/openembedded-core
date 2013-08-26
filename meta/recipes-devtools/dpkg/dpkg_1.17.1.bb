require dpkg.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI += "file://noman.patch \
            file://check_snprintf.patch \
            file://check_version.patch \
            file://preinst.patch \
            file://fix-timestamps.patch \
            file://remove-tar-no-timestamp.patch \
            file://fix-abs-redefine.patch \
            file://arch_pm.patch \
           "

SRC_URI[md5sum] = "ece3ae87a099158c17bde95c0036c575"
SRC_URI[sha256sum] = "8912ea77bc9c14297c0a340f5f461fbd212582ce814e1805d1d0436ca885e3a1"

