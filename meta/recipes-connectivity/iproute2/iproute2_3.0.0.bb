require iproute2.inc

#v3.0.0 tag
SRCREV = "ce691fb5ce78b2c75243c60a757a3990ae09681c"
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/shemminger/iproute2.git \
           file://configure-cross.patch"
S = "${WORKDIR}/git"

