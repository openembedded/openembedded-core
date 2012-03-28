require genext2fs.inc

PR = "r2"

SRC_URI += "file://update_to_1.95.patch \
            file://0001-Fix-warnings-remove-some-unused-macros.patch \
            file://0002-Add-put_blk-and-put_nod-routines.patch \
            file://0003-Add-get_blkmap-and-put_blkmap.patch \
            file://0004-Add-a-dirwalker-for-walking-through-directory-entrie.patch \
            file://0005-Make-filesystem-struct-not-an-overloay.patch \
            file://0006-Improve-the-efficiency-of-extend_blk.patch \
            file://0007-Move-hdlinks-into-the-filesystem-structure.patch \
            file://0008-Separate-out-the-creation-of-the-filesystem-structur.patch \
            file://0009-Move-byte-swapping-into-the-get-put-routines.patch \
            file://0010-Convert-over-to-keeping-the-filesystem-on-disk.patch \
            file://0011-Copy-files-into-the-filesystem-a-piece-at-a-time.patch \
            file://0012-Add-rev-1-support-large-file-support-and-rework-hole.patch \
            file://0013-Add-volume-id-support.patch \
            file://0014-Remove-unneeded-setting-of-s_reserved.patch \
            file://0015-Rework-creating-the-lost-found-directory.patch \
            file://0016-Fix-the-documentation-for-the-new-L-option.patch \
            file://0017-Fix-file-same-comparison.patch \
            file://0018-Handle-files-changing-while-we-are-working.patch \
            file://0019-Make-sure-superblock-is-clear-on-allocation.patch \
            file://fix-nbblocks-cast.patch"

SRC_URI[md5sum] = "b7b6361bcce2cedff1ae437fadafe53b"
SRC_URI[sha256sum] = "404dbbfa7a86a6c3de8225c8da254d026b17fd288e05cec4df2cc7e1f4feecfc"
