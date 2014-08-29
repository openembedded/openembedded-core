SUMMARY = "Text file viewer similar to more"
DESCRIPTION = "Less is a program similar to more, i.e. a terminal \
based program for viewing text files and the output from other \
programs. Less offers many features beyond those that more does."
HOMEPAGE = "http://www.greenwoodsoftware.com/"
SECTION = "console/utils"

# (GPLv2+ (<< 418), GPLv3+ (>= 418)) | less
# Including email author giving permissing to use BSD
#
# From: Mark Nudelman <markn@greenwoodsoftware.com>
# To: Elizabeth Flanagan <elizabeth.flanagan@intel.com
# Date: 12/19/11
#
# Hi Elizabeth,
# Using a generic BSD license for less is fine with me.
# Thanks,
#
# --Mark
#

LICENSE = "GPLv3+ | BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://LICENSE;md5=866cc220f330b04ae4661fc3cdfedea7"
DEPENDS = "ncurses"

SRC_URI = "http://www.greenwoodsoftware.com/${BPN}/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "7bc444bdb751bcf80ed0c39c17b4fcf4"
SRC_URI[sha256sum] = "8305f5a06764ff094ed3428f94f29059541e2483eae201991f90823da6557212"

inherit autotools update-alternatives

do_install () {
        oe_runmake 'bindir=${D}${bindir}' 'mandir=${D}${mandir}' install
}

ALTERNATIVE_${PN} = "less"
ALTERNATIVE_PRIORITY = "100"
