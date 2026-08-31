# FIXME: the LIC_FILES_CHKSUM values have been updated by 'devtool upgrade'.
# The following is the difference between the old and the new license text.
# Please update the LICENSE value if needed, and summarize the changes in
# the commit message via 'License-Update:' tag.
# (example: 'License-Update: copyright years updated.')
#
# The changes:
#
# --- LICENSES
# +++ LICENSES
# @@ -61,6 +61,36 @@
#  
#  #+begin_quote
#     Copyright (c) 2021-2022, Intel Corporation
# +
# +   Redistribution and use in source and binary forms, with or without
# +   modification, are permitted provided that the following conditions are met:
# +
# +       * Redistributions of source code must retain the above copyright notice,
# +         this list of conditions and the following disclaimer.
# +       * Redistributions in binary form must reproduce the above copyright
# +         notice, this list of conditions and the following disclaimer in the
# +         documentation and/or other materials provided with the distribution.
# +       * Neither the name of Intel Corporation nor the names of its contributors
# +         may be used to endorse or promote products derived from this software
# +         without specific prior written permission.
# +
# +   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# +   AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# +   IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
# +   DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
# +   FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
# +   DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
# +   SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
# +   CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
# +   OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
# +   OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
# +#+end_quote
# +
# +  For files:
# +  - cipher/sm3-intel-avx2-amd64.S
# +
# +#+begin_quote
# +   Copyright (c) 2023-2024, Intel Corporation
#  
#     Redistribution and use in source and binary forms, with or without
#     modification, are permitted provided that the following conditions are met:
# 
#

SUMMARY = "General purpose cryptographic library based on the code from GnuPG"
DESCRIPTION = "A cryptography library developed as a separated module of GnuPG. \
It can also be used independently of GnuPG, but depends on its error-reporting \
library Libgpg-error."
HOMEPAGE = "http://directory.fsf.org/project/libgcrypt/"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"
SECTION = "libs"

LICENSE = "BSD-3-Clause AND GPL-2.0-or-later AND LGPL-2.1-or-later"
LICENSE:${PN} = "BSD-3-Clause AND LGPL-2.1-or-later"
LICENSE:${PN}-dev = "GPL-2.0-or-later AND LGPL-2.1-or-later"

LIC_FILES_CHKSUM = "file://COPYING;md5=570a9b3749dd0463a1778803b12a6dce \
                    file://COPYING.LIB;md5=4bf661c1e3793e55c8d1051bc5e0ae21 \
                    file://LICENSES;md5=47e346a71a2593d19f741408407fe91f \
                    "

DEPENDS = "libgpg-error"

UPSTREAM_CHECK_URI = "https://gnupg.org/download/index.html"
SRC_URI = "${GNUPG_MIRROR}/libgcrypt/libgcrypt-${PV}.tar.bz2 \
           file://0001-libgcrypt-fix-m4-file-for-oe-core.patch \
           file://0004-tests-Makefile.am-fix-undefined-reference-to-pthread.patch \
           file://0001-tests-Fix-link-errors-for-t-thread-local.patch \
           file://no-native-gpg-error.patch \
           file://no-bench-slope.patch \
           file://run-ptest \
           "
SRC_URI[sha256sum] = "98d1b0b3202d2b03fa754a35aa3cbbfcf526a3260d8d2ee213748001b1043006"

BINCONFIG = "${bindir}/libgcrypt-config"

inherit autotools texinfo binconfig-disabled pkgconfig ptest upstream-stable-release-point

require recipes-support/gnupg/drop-unknown-suffix.inc

EXTRA_OECONF = "--disable-asm"
EXTRA_OEMAKE:class-target = "LIBTOOLFLAGS='--tag=CC'"

PACKAGECONFIG ??= "capabilities"
PACKAGECONFIG[capabilities] = "--with-capabilities,--without-capabilities,libcap"

do_configure:prepend () {
	# Else this could be used in preference to the one in aclocal-copy
	rm -f ${S}/m4/gpg-error.m4
}

do_install_ptest() {
    cd tests
    oe_runmake testdrv-build testdrv
    install testdrv $(srcdir=${S}/tests ./testdrv-build --files | sort | uniq) ${D}${PTEST_PATH}
}

FILES:${PN}-dev += "${bindir}/hmac256 ${bindir}/dumpsexp"

BBCLASSEXTEND = "native nativesdk"
