RDEPENDS_${PN} += "patch diffstat bzip2"

include quilt.inc

inherit autotools gettext

include quilt-package.inc
