DESCRIPTION = "The MusicBrainz client is a library which can be built into other programs.  The library allows you to access the data held on the MusicBrainz server."
HOMEPAGE = "http://musicbrainz.org"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.txt;md5=fbc093901857fcd118f065f900982c24 \
                    file://include/musicbrainz3/includes.h;beginline=1;endline=21;md5=e7d3b3e6d8bb7ee278dc4040d380ebd5"
DEPENDS = "expat neon"

PR = "r0"

SRC_URI = "http://ftp.musicbrainz.org/pub/musicbrainz/${PN}-${PV}.tar.gz"

inherit cmake pkgconfig
