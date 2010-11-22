addtask listtasks
do_listtasks[nostamp] = "1"
python do_listtasks() {
	import sys
	# emit variables and shell functions
	#bb.data.emit_env(sys.__stdout__, d)
	# emit the metadata which isnt valid shell
	for e in d.keys():
		if bb.data.getVarFlag(e, 'task', d):
			sys.__stdout__.write("%s\n" % e)
}

CLEANFUNCS ?= ""

addtask clean
do_clean[nostamp] = "1"
python do_clean() {
	"""clear the build and temp directories"""
	dir = bb.data.expand("${WORKDIR}", d)
	bb.note("Removing " + dir)
	oe.path.remove(dir)

	dir = "%s.*" % bb.data.expand(bb.data.getVar('STAMP', d), d)
	bb.note("Removing " + dir)
	oe.path.remove(dir)

	for f in (bb.data.getVar('CLEANFUNCS', d, 1) or '').split():
		bb.build.exec_func(f, d)
}

addtask rebuild after do_${BB_DEFAULT_TASK}
do_rebuild[dirs] = "${TOPDIR}"
do_rebuild[nostamp] = "1"
python do_rebuild() {
	"""rebuild a package"""
}

#addtask mrproper
#do_mrproper[dirs] = "${TOPDIR}"
#do_mrproper[nostamp] = "1"
#python do_mrproper() {
#	"""clear downloaded sources, build and temp directories"""
#	dir = bb.data.expand("${DL_DIR}", d)
#	if dir == '/': bb.build.FuncFailed("wrong DATADIR")
#	bb.debug(2, "removing " + dir)
#	os.system('rm -rf ' + dir)
#	bb.build.exec_func('do_clean', d)
#}

addtask checkuri
do_checkuri[nostamp] = "1"
python do_checkuri() {
	import sys

	localdata = bb.data.createCopy(d)
	bb.data.update_data(localdata)

	src_uri = bb.data.getVar('SRC_URI', localdata, 1)

	try:
		bb.fetch.init(src_uri.split(),d)
	except bb.fetch.NoMethodError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("No method: %s" % value)

	try:
		bb.fetch.checkstatus(localdata)
	except bb.fetch.MissingParameterError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("Missing parameters: %s" % value)
	except bb.fetch.FetchError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("Fetch failed: %s" % value)
	except bb.fetch.MD5SumError:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("MD5  failed: %s" % value)
	except:
		(type, value, traceback) = sys.exc_info()
		raise bb.build.FuncFailed("Unknown fetch Error: %s" % value)
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
