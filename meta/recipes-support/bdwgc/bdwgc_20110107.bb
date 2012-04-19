SUMMARY = "A garbage collector for C and C++"

DESCRIPTION = "The Boehm-Demers-Weiser conservative garbage collector can be\
 used as a garbage collecting replacement for C malloc or C++ new. It allows\
 you to allocate memory basically as you normally would, without explicitly\
 deallocating memory that is no longer useful. The collector automatically\
 recycles memory when it determines that it can no longer be otherwise\
 accessed.\
  The collector is also used by a number of programming language\
 implementations that either use C as intermediate code, want to facilitate\
 easier interoperation with C libraries, or just prefer the simple collector\
 interface.\
  Alternatively, the garbage collector may be used as a leak detector for C\
 or C++ programs, though that is not its primary goal.\
  Empirically, this collector works with most unmodified C programs, simply\
 by replacing malloc with GC_malloc calls, replacing realloc with GC_realloc\
 calls, and removing free calls."

HOMEPAGE = "http://www.hpl.hp.com/personal/Hans_Boehm/gc/"
SECTION = "devel"
LICENSE = "MIT & FSF-Unlimited & GPL-2.0"
LIC_FILES_CHKSUM = "file://README.QUICK;md5=9b9dd874f6940641b6ab19893ee8f1cc \
                    file://doc/README;md5=6d74ce18ff55d936a4d6bd35a98e9eb9 \
                    file://libatomic_ops/doc/LICENSING.txt;md5=607073e04548eac7d1f763e480477bab"

SRC_URI = "http://www.hpl.hp.com/personal/Hans_Boehm/gc/gc_source/bdwgc-7_2alpha5-20110107.tar.bz2"

SRC_URI[md5sum] = "4f3c130fc71ff23584edaa19a37739ee"
SRC_URI[sha256sum] = "1f57b959ae1144e1e5fa59d52d7cb4ed626fd74cf47da1c9c119b8b46ae2722d"
FILES_${PN}-doc = "/usr/share"

PR = "r1"

S = "${WORKDIR}/bdwgc"

ARM_INSTRUCTION_SET = "arm"

inherit autotools
BBCLASSEXTEND = "native nativesdk"
