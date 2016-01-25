# Copyright (C) 2015 Intel Corporation
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Make public keys of the signing keys available"
LICENSE = "MIT"
PACKAGES = ""

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
do_package[noexec] = "1"
do_packagedata[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_rpm[noexec] = "1"
do_package_write_deb[noexec] = "1"
do_populate_sysroot[noexec] = "1"

EXCLUDE_FROM_WORLD = "1"


python do_export_public_keys () {
    from oe.gpg_sign import get_signer

    if d.getVar("RPM_SIGN_PACKAGES", True):
        # Export public key of the rpm signing key
        signer = get_signer(d,
                            d.getVar('RPM_GPG_BACKEND', True),
                            d.getVar('RPM_GPG_NAME', True),
                            d.getVar('RPM_GPG_PASSPHRASE_FILE', True))
        signer.export_pubkey(d.getVar('RPM_GPG_PUBKEY', True))

    if d.getVar('PACKAGE_FEED_SIGN', True) == '1':
        # Export public key of the feed signing key
        signer = get_signer(d,
                            d.getVar('PACKAGE_FEED_GPG_BACKEND', True),
                            d.getVar('PACKAGE_FEED_GPG_NAME', True),
                            d.getVar('PACKAGE_FEED_GPG_PASSPHRASE_FILE', True))
        signer.export_pubkey(d.getVar('PACKAGE_FEED_GPG_PUBKEY', True))
}
addtask do_export_public_keys before do_build
