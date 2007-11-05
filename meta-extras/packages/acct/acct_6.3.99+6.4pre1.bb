LICENSE = "GPL"
DESCRIPTION = "GNU Accounting Utilities - user and process accounting."

SRC_URI = "http://www.physik3.uni-rostock.de/tim/kernel/utils/acct/acct-6.4-pre1.tar.gz \
	file://cross-compile.patch;patch=1"

S = "${WORKDIR}/acct-6.4-pre1"

inherit autotools
