PRSERV_DUMPOPT_VERSION = "${PRAUTOINX}"
PRSERV_DUMPOPT_PKGARCH  = ""
PRSERV_DUMPOPT_CHECKSUM = ""
PRSERV_DUMPOPT_COL = "0"

PRSERV_DUMPDIR ??= "${LOG_DIR}/db"
PRSERV_DUMPFILE ??= "${PRSERV_DUMPDIR}/prserv.inc"

python prexport_handler () {
    import bb.event
    if not e.data:
        return

    if isinstance(e, bb.event.RecipeParsed):
        import oe.prservice
        #get all PR values for the current PRAUTOINX
        ver = e.data.getVar('PRSERV_DUMPOPT_VERSION', True)
        ver = ver.replace('%','-')
        retval = oe.prservice.prserv_dump_db(e.data)
        if not retval:
            bb.fatal("prexport_handler: export failed!")
        (metainfo, datainfo) = retval
        if not datainfo:
            bb.warn("prexport_handler: No AUROPR values found for %s" % ver)
            return
        oe.prservice.prserv_export_tofile(e.data, None, datainfo, False)
    elif isinstance(e, bb.event.ParseStarted):
        import bb.utils
        import oe.prservice
        oe.prservice.prserv_check_avail(e.data)
        #remove dumpfile
        bb.utils.remove(e.data.getVar('PRSERV_DUMPFILE', True))
    elif isinstance(e, bb.event.ParseCompleted):
        import oe.prservice
        #dump meta info of tables
        d = e.data.createCopy()
        d.setVar('PRSERV_DUMPOPT_COL', "1")
        retval = oe.prservice.prserv_dump_db(d)
        if not retval:
            bb.error("prexport_handler: export failed!")
            return
        (metainfo, datainfo) = retval
        oe.prservice.prserv_export_tofile(d, metainfo, None, True)

}

addhandler prexport_handler
