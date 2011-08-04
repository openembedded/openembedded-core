SUMMARY = "MusicBrainz Client"
DESCRIPTION = "The MusicBrainz client is a library which can be built into other programs.  The library allows you to access the data held on the MusicBrainz server."
HOMEPAGE = "http://musicbrainz.org"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.txt;md5=fbc093901857fcd118f065f900982c24 \
                    file://include/musicbrainz3/includes.h;beginline=1;endline=21;md5=9ab2846573f560cea7561da472803a72"
DEPENDS = "expat neon"

PR = "r3"

SRC_URI = "http://ftp.musicbrainz.org/pub/musicbrainz/${BPN}-${PV}.tar.gz \
           file://fix_build_issue_for_gcc_4.5.0.patch \
           file://allow-libdir-override.patch "

SRC_URI[md5sum] = "f4824d0a75bdeeef1e45cc88de7bb58a"
SRC_URI[sha256sum] = "7fd459a9fd05be9faec60a9a21caa9a1e9fda03147d58d8c7c95f33582a738c5"

inherit cmake pkgconfig

EXTRA_OECMAKE = "-DLIB_INSTALL_DIR:PATH=${libdir}"
