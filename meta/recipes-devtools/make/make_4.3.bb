LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
require make.inc

SRC_URI += "\
	file://0001-m4-getloadavg.m4-restrict-AIX-specific-test-on-AIX.patch \
	file://0002-modules-fcntl-allow-being-detected-by-importing-proj.patch \
	file://0001-src-dir.c-fix-buffer-overflow-warning.patch \
	file://0002-w32-compat-dirent.c-follow-header.patch \
	file://0003-posixfcn-fcntl-gnulib-make-emulated.patch \
"

EXTRA_OECONF += "--without-guile"

SRC_URI[md5sum] = "d5c40e7bd1e97a7404f5d3be982f479a"
SRC_URI[sha256sum] = "de1a441c4edf952521db30bfca80baae86a0ff1acd0a00402999344f04c45e82"

BBCLASSEXTEND = "native nativesdk"
