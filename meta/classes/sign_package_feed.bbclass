# Class for signing package feeds
#
# Related configuration variables that will be used after this class is
# iherited:
# PACKAGE_FEED_PASSPHRASE_FILE
#           Path to a file containing the passphrase of the signing key.
# PACKAGE_FEED_GPG_NAME
#           Name of the key to sign with. May be key id or key name.
# PACKAGE_FEED_GPG_BACKEND
#           Optional variable for specifying the backend to use for signing.
#           Currently the only available option is 'local', i.e. local signing
#           on the build host.
# GPG_BIN
#           Optional variable for specifying the gpg binary/wrapper to use for
#           signing.
# GPG_PATH
#           Optional variable for specifying the gnupg "home" directory:
#
inherit sanity

PACKAGE_FEED_SIGN = '1'
PACKAGE_FEED_GPG_BACKEND ?= 'local'


python () {
    # Check sanity of configuration
    for var in ('PACKAGE_FEED_GPG_NAME', 'PACKAGE_FEED_GPG_PASSPHRASE_FILE'):
        if not d.getVar(var, True):
            raise_sanity_error("You need to define %s in the config" % var, d)
}

do_package_index[depends] += "signing-keys:do_deploy"
do_rootfs[depends] += "signing-keys:do_populate_sysroot"
