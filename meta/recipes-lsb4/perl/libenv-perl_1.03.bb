SUMMARY = "Perl module that imports environment variables as scalars or arrays."
DESCRIPTION = "Perl maintains environment variables in a special hash named %ENV. \
For when this access method is inconvenient, the Perl module Env allows environment \
variables to be treated as scalar or array variables."

HOMEPAGE = "http://search.cpan.org/~flora/Env/"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"
PR = "r0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=421c6c0cda752f4977b8287ecebf6061"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/F/FL/FLORA/Env-${PV}.tar.gz"

SRC_URI[md5sum] = "471070589ae99415d8956e042c2f12da"
SRC_URI[sha256sum] = "de4134ed0c4485e64e7f0fe3e65bab81917cb400cc0d316979df8322d0bdaecc"

S = "${WORKDIR}/Env-${PV}"

inherit cpan

BBCLASSEXTEND = "native"
