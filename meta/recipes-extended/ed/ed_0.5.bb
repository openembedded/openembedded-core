DESCRIPTION = "GNU ed is a line-oriented text editor"
HOMEPAGE = "http://www.gnu.org/software/ed/"
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=6ddd5335ef96fb858a138230af773710 \
                    file://main.c;beginline=1;endline=17;md5=36d4b85e5ae9028e918d1cc775c2475e"

PR = "r1"
SRC_URI = "http://download.savannah.gnu.org/releases-noredirect/ed/ed-${PV}.tar.bz2"

inherit autotools

CONFIGUREOPTS := "${@d.getVar('CONFIGUREOPTS', True).replace('--disable-silent-rules', ' ')}"
