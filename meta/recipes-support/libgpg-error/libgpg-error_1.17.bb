SUMMARY = "Small library that defines common error values for all GnuPG components"
HOMEPAGE = "http://www.gnupg.org/related_software/libgpg-error/"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"

LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/gpg-error.h.in;endline=23;md5=5dfe776dc8b62af093ddc859de6f494c \
                    file://src/init.c;endline=20;md5=8f5a9b59634f4aebcd0ec9d3ebd53bfe"


SECTION = "libs"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/libgpg-error/libgpg-error-${PV}.tar.bz2 \
           file://pkgconfig.patch"

SRC_URI[md5sum] = "b4f8d8b9ff14aed41f279aa844563539"
SRC_URI[sha256sum] = "3ff4e5a71116eb862cd14185fcd282850927b8608e3b4186834fd940fbef57b5"

BINCONFIG = "${bindir}/gpg-error-config"

inherit autotools binconfig-disabled pkgconfig gettext

do_compile_prepend() {
	TARGET_FILE=linux-gnu
	if [ ${TARGET_OS} != "linux" ]; then
		TARGET_FILE=${TARGET_OS}
	fi

	case ${TARGET_ARCH} in
	  aarch64)    TUPLE=aarch64-unknown-linux-gnu ;;
	  arm)	      TUPLE=arm-unknown-linux-gnueabi ;;
	  armeb)      TUPLE=arm-unknown-linux-gnueabi ;;
	  i586)       TUPLE=i486-pc-linux-gnu ;;
	  mipsel)     TUPLE=mipsel-unknown-linux-gnu ;;
	  mips64el)   TUPLE=mipsel-unknown-linux-gnu ;;
	  mips64)     TUPLE=mips-unknown-linux-gnu ;;
	  mips)       TUPLE=mips-unknown-linux-gnu ;;
	  powerpc64)  TUPLE=powerpc64-unknown-linux-gnu ;;
	  powerpc)    TUPLE=powerpc-unknown-linux-gnu ;;
	  sh4)	      TUPLE=sh4-unknown-linux-gnu ;;
	  x86_64)     TUPLE=x86_64-pc-linux-gnu ;;
	esac

	cp ${S}/src/syscfg/lock-obj-pub.$TUPLE.h \
	  ${S}/src/syscfg/lock-obj-pub.$TARGET_FILE.h
}

do_install_append() {
	# we don't have common lisp in OE
	rm -rf "${D}${datadir}/common-lisp/"
}

FILES_${PN}-dev += "${bindir}/gpg-error"

BBCLASSEXTEND = "native"
