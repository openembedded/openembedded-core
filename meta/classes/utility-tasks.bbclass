addtask listtasks
do_listtasks[nostamp] = "1"
python do_listtasks() {
	import sys
	# emit variables and shell functions
	#bb.data.emit_env(sys.__stdout__, d)
	# emit the metadata which isnt valid shell
	for e in d.keys():
		if d.getVarFlag(e, 'task'):
			bb.plain("%s" % e)
}

CLEANFUNCS ?= ""

addtask clean
do_clean[nostamp] = "1"
python do_clean() {
	"""clear the build and temp directories"""
	dir = d.expand("${WORKDIR}")
	bb.note("Removing " + dir)
	oe.path.remove(dir)

	dir = "%s.*" % bb.data.expand(d.getVar('STAMP'), d)
	bb.note("Removing " + dir)
	oe.path.remove(dir)

	for f in (d.getVar('CLEANFUNCS', True) or '').split():
		bb.build.exec_func(f, d)
}

addtask checkuri
do_checkuri[nostamp] = "1"
python do_checkuri() {
	src_uri = (d.getVar('SRC_URI', True) or "").split()
	if len(src_uri) == 0:
		return

	localdata = bb.data.createCopy(d)
	bb.data.update_data(localdata)

        try:
            fetcher = bb.fetch2.Fetch(src_uri, localdata)
            fetcher.checkstatus()
        except bb.fetch2.BBFetchException, e:
            raise bb.build.FuncFailed(e)
}

addtask checkuriall after do_checkuri
do_checkuriall[recrdeptask] = "do_checkuri"
do_checkuriall[nostamp] = "1"
do_checkuriall() {
	:
}

addtask fetchall after do_fetch
do_fetchall[recrdeptask] = "do_fetch"
do_fetchall() {
	:
}

addtask buildall after do_build
do_buildall[recrdeptask] = "do_build"
do_buildall() {
	:
}
