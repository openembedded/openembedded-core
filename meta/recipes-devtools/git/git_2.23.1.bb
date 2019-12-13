require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "5dbd2a5bf2c4aeec65a7b34229ba47f2"
SRC_URI[tarball.sha256sum] = "d24f129cb0d395550b9ba62f518200405e9fee422134cb46b9820f2db2f54b47"
SRC_URI[manpages.md5sum] = "890a45439e8462460a590aeb7fe7725c"
SRC_URI[manpages.sha256sum] = "efd51b6078469501ccbe2e39c5565ba696e9bdf213453ac477be0c256fc7ad42"
