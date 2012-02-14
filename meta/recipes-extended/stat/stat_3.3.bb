SECTION = "console/utils"
DESCRIPTION = "Display all information about a file that the stat() call provides and all information a filesystem that statfs() provides."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=39886b077fd072e876e5c4c16310b631 \
                    file://GPL;md5=94d55d512a9ba36caa9b7df079bae19f"

BBCLASSEXTEND = "native"

PR = "r0"

SRC_URI = "ftp://metalab.unc.edu/pub/Linux/utils/file/stat-3.3.tar.gz"
SRC_URI[md5sum] = "37e247e8e400ad9205f1b0500b728fd3"
SRC_URI[sha256sum] = "7071f0384a423a938dd542c1f08547a02824f6359acd3ef3f944b2c4c2d1ee09"

do_install() {
	install -d ${D}${bindir} ${D}${mandir}/man1
	install -m 755 stat ${D}${bindir}
	install -m 644 stat.1 ${D}${mandir}/man1
}

#do_install_virtclass-native() {
#	install -d ${D}${bindir}
#	install -m 755 stat ${D}${bindir}
#}

