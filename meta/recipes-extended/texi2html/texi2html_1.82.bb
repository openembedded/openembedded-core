DESCRIPTION = "Perl script that converts Texinfo to HTML"
HOMEPAGE    = "http://www.nongnu.org/texi2html/"
SECTION     = "console/utils"
LICENSE     = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

PR = "r0"

SRC_URI     = "http://download.savannah.gnu.org/releases/texi2html/${P}.tar.bz2"

SRC_URI[md5sum] = "a8a9193c0ac1bec2f3ca7be40a5a82eb"
SRC_URI[sha256sum] = "d69c1effc416896409003ea64fdb21152cc0a9a7c665d437a0a3bef9b588b4f1"

inherit autotools
