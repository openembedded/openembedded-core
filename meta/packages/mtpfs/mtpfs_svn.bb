DESCRIPTION = "FUSE file system for MTP devices"
HOMEPAGE = "http://libmtp.sourceforge.net"
SECTION = "libs"
PROVIDES = "mtpfs"
DEPENDS = "fuse libmtp libmad libid3tag"
LICENSE = "GPL"

PR = "r0"

SRCREV = "${AUTOREV}"

SRC_URI = "svn://mtpfs.googlecode.com/svn/;module=trunk;proto=http"
 
inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}

S = "${WORKDIR}/trunk"