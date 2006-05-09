include qemu_cvs.bb
inherit native
prefix = "${STAGING_DIR}/${BUILD_SYS}"

python __anonymous() {
    from bb import which, data
	
    path = data.getVar('PATH', d)
    if len(which(path, 'gcc-3.4')) != 0:
        data.setVar('EXTRA_OECONF', " --cc=gcc-3.4", d)
    elif len(which(path, 'gcc-3.3')) != 0:
        data.setVar('EXTRA_OECONF', " --cc=gcc-3.3", d)

}
