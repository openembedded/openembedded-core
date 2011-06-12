BROKEN = "1"

DESCRIPTION = "Alsa Tools"
SECTION = "console/utils"
LICENSE = "GPLv2"
DEPENDS = "alsa-lib ncurses"

PR = "r1"

LIC_FILES_CHKSUM = "file://hdsploader/COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "ftp://ftp.alsa-project.org/pub/tools/alsa-tools-${PV}.tar.bz2"

SRC_URI[md5sum] = "08fe93a12006093e590d7ecc02b119dd"
SRC_URI[sha256sum] = "17d43de93ab2db98886d89a53e45341daa46a4ef6edd405db87f4b5a5dc64a05"

inherit autotools
