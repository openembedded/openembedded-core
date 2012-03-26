# This file is used for archiving sources ,patches,and logs to tarball.
# It also output building environment to xxx.dump.data and create xxx.diff.gz to record 
# all content in ${S} to a diff file. 

EXCLUDE_FROM ?= ".pc autom4te.cache"
ARCHIVE_TYPE ?= "TAR SRPM"
DISTRO ?= "poky"
PATCHES_ARCHIVE_WITH_SERIES = 'TRUE'

def parse_var(d,var):
	''' parse variable like ${PV}  in "require xxx_${PV}.inc"  to a real value. for example, change "require xxx_${PV}.inc" to "require xxx_1.2.inc" '''
	import re 
	pat = re.compile('.*\$({(.*)}).*')
	if '$' not in var and '/' not in var:
		return var
	else:
		if '/' in var:
			return [i for i in var.split('/') if i.endswith('.inc')][0]
		elif '$' in  var:
			m = pat.match(var)
			patstr = '\$' + m.group(1)
			var_str = m.group(2)
			return re.sub(patstr,d.getVar(var_str,True),var)
		else:
			return var
		
def get_bb_inc(d):
	'''create a directory "script-logs" including .bb and .inc file in ${WORKDIR}'''
	import re
	import os
	import shutil
	
	bbinc = []
	pat=re.compile('require\s*([^\s]*\.*)(.*)')
	file_dir = d.getVar('FILE', True)
	bbdir = os.path.dirname(file_dir)
	work_dir = d.getVar('WORKDIR', True)
	os.chdir(work_dir)
	bb.mkdirhier("script-logs")
	os.chdir(bbdir)
	bbfile = os.path.basename(file_dir)
	bbinc.append(bbfile)
	
	def get_inc (file):
		f = open(file,'r')
		for line in f.readlines():
			if 'require' not  in line:
				bbinc.append(file)
			else:
				try:
					incfile = pat.match(line).group(1)
					incfile = parse_var(d,incfile)
					bbinc.append(incfile)
					get_inc(incfile)
				except (IOError,AttributeError):
					pass
	get_inc(bbfile)
	os.chdir(work_dir)
	for root, dirs, files in os.walk(bbdir):
		for file in bbinc:
			if file in files:
				shutil.copy(root + '/' + file,'script-logs')
	oe.path.copytree('temp', 'script-logs')
	return work_dir + '/script-logs'

def get_all_patches(d):
	'''copy patches and series file to a pointed directory which will be archived to tarball in ${WORKDIR}'''
	import shutil
	
	src_patches=[]
	pf = d.getVar('PF', True)
	work_dir = d.getVar('WORKDIR', True)
	s = d.getVar('S',True)
	dest = os.path.join(work_dir, pf + '-patches') 
	shutil.rmtree(dest, ignore_errors=True)
	bb.mkdirhier(dest)
	
	src_uri = d.getVar('SRC_URI', 1).split()
	fetch = bb.fetch2.Fetch(src_uri, d)
	locals = (fetch.localpath(url) for url in fetch.urls)
	for local in locals:
		src_patches.append(local)
	if not cmp(work_dir,s):
		tmp_list = src_patches
	else:
		tmp_list = src_patches[1:]
	
	for patch in tmp_list:
		try:
			shutil.copy(patch,dest)
		except IOError:
			if os.path.isdir(patch):
				oe.path.copytree(patch,dest)	
	return dest

def get_applying_patches(d):
	"""only copy applying patches to a pointed directory which will be archived to tarball"""
	import os
	import shutil


	pf = d.getVar('PF', True)
	work_dir = d.getVar('WORKDIR', True)
	dest = os.path.join(work_dir, pf + '-patches') 
	shutil.rmtree(dest, ignore_errors=True)
	bb.mkdirhier(dest)


	patches = src_patches(d)
	for patch in patches:
		_, _, local, _, _, parm = bb.decodeurl(patch)
		if local:
			 shutil.copy(local,dest)
	return dest

def not_tarball(d):
	'''packages including key words 'work-shared','native', 'task-' will be passed'''
	import os

	workdir = d.getVar('WORKDIR',True)
	s = d.getVar('S',True)
	if 'work-shared' in s or 'task-' in workdir or 'native' in workdir:
		return True
	else:
		return False

def get_source_from_downloads(d,middle_name):
	'''copy tarball of $P to $WORKDIR when this tarball exists in $DL_DIR'''
	if middle_name in 'patched' 'configured':
		return
	pf = d.getVar('PF', True)
	dl_dir = d.getVar('DL_DIR',True)
	try:
		source = os.path.basename(d.getVar('SRC_URI', 1).split()[0])
		os.chdir(dl_dir)
		if os.path.exists(source) and not os.path.isdir(source):
			return source
	except (IndexError, OSError):
		pass
	
def archive_sources_from_directory(d,middle_name): 
	'''archive sources codes tree to tarball when tarball of $P doesn't exist in $DL_DIR'''
	import tarfile
	import shutil
	
	s = d.getVar('S',True)
	workdir=d.getVar('WORKDIR', True)
	PF = d.getVar('PF',True)
	tarname = PF + '-' + middle_name + ".tar.gz"

	if os.path.exists(s) and s is not workdir:
		sourcedir = os.path.basename(s)
		tarbase = os.path.dirname(s)
		if not sourcedir or os.path.dirname(tarbase) == workdir:
			sourcedir = os.path.basename(os.path.dirname(s))
			tarbase = os.path.dirname(os.path.dirname(s))
		os.chdir(tarbase)
	else:
		sourcedir = os.path.basename(s)
		if not os.path.exists(sourcedir):
			os.mkdir(sourcedir)
		try:
			for file in os.listdir(s):
				if file is not 'temp' and file is not sourcedir:
					shutil.copy(file,sourcedir)
		except (IOError,OSError):
			pass

	if (len(os.listdir(sourcedir))) != 0:
		tar = tarfile.open( tarname, "w:gz")
		tar.add(sourcedir)
		tar.close()
		if cmp(workdir,os.path.dirname(s)) and not os.path.exists(workdir + '/' + tarname):
			shutil.move(os.path.dirname(s) + '/' + tarname,workdir)
	else:
		return
	return tarname

def archive_sources(d,middle_name):
	'''copy tarball from $DL_DIR to $WORKDIR if have tarball, archive source codes tree in $WORKDIR if $P is directory instead of tarball'''
	import shutil
	work_dir = d.getVar('WORKDIR',True)
	file = get_source_from_downloads(d,middle_name)
	if file:
		shutil.copy(file,work_dir)
	else:
		file = archive_sources_from_directory(d,middle_name)
	return file


def archive_patches(d,patchdir,series):
	'''archive patches to tarball and also include series files if 'series' is True'''
	import tarfile
	import shutil

	s = d.getVar('S',True)
	work_dir = d.getVar('WORKDIR', True)
	os.chdir(work_dir)
	patch_dir = os.path.basename(patchdir)
	tarname = patch_dir + ".tar.gz"
	if series  == 'all' and os.path.exists(s + '/patches/series'):
			shutil.copy(s + '/patches/series',patch_dir)
	tar = tarfile.open(tarname, "w:gz")
	tar.add(patch_dir)
	tar.close()
	shutil.rmtree(patch_dir, ignore_errors=True)
	return tarname

def select_archive_patches(d,option):
	'''select to archive all patches including non-applying and series or applying patches '''
	if option == "all":
		patchdir = get_all_patches(d)
	elif option == "applying":
		patchdir = get_applying_patches(d)
	try:
		os.rmdir(patchdir)
	except OSError:
			tarpatch = archive_patches(d,patchdir,option)
			return tarpatch
	return

def archive_logs(d,logdir,bbinc=False):
	'''archive logs in temp to tarball and .bb and .inc files if bbinc is True '''
	import tarfile
	import shutil

	log_dir = os.path.basename(logdir)
	pf = d.getVar('PF',True)
	tarname = pf + '-' + log_dir + ".tar.gz"
	tar = tarfile.open(tarname, "w:gz")
	tar.add(log_dir)
	tar.close()
	if bbinc:
		shutil.rmtree(log_dir, ignore_errors=True)
	return tarname 

def get_licenses(d):
	'''get licenses for running .bb file'''
	licenses = d.getVar('LICENSE', 1).replace('&', '|')
	licenses = licenses.replace('(', '').replace(')', '') 
	clean_licenses = ""
	for x in licenses.split():
		if x.strip() == '' or x == 'CLOSED':
			continue
		if x != "|":
			clean_licenses += x
	if '|' in clean_licenses:
		clean_licenses = clean_licenses.replace('|','')
	return clean_licenses
	

def move_tarball_deploy(d,tarball_list):
	'''move tarball in location to ${DEPLOY_DIR}/sources'''
	import shutil
	
	if tarball_list is []:
		return
	target_sys = d.getVar('TARGET_SYS', True)
	pf = d.getVar('PF', True)
	licenses = get_licenses(d)
	tar_sources = d.getVar('DEPLOY_DIR', True) + '/sources/' + target_sys + '/' + licenses + '/' + pf
	if not os.path.exists(tar_sources):
		bb.mkdirhier(tar_sources)
	for source in tarball_list:
		if source:
			if os.path.exists(tar_sources + '/' + source):
				os.remove(tar_sources + '/' + source)
			shutil.move(source,tar_sources)

def verify_var(d):
	'''check the type for archiving package('tar' or 'srpm')'''
	try:
		if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True).upper() not in d.getVar('ARCHIVE_TYPE', True).split():
			raise AttributeError	
	except AttributeError:
			bb.fatal("\"SOURCE_ARCHIVE_PACKAGE_TYPE\" is \'tar\' or \'srpm\', no other types") 

def store_package(d,package_name):
	'''store tarbablls name to file "tar-package"'''
	try:
		f = open(d.getVar('WORKDIR',True )+ '/tar-package','a')
		f.write(package_name + ' ')
		f.close()
	except IOError:
		pass

def get_package(d):
	'''get tarballs name from "tar-package"'''
	try:
		os.chdir(d.getVar('WORKDIR', True))
		f = open('tar-package','r')
		line = list(set(f.readline().replace('\n','').split()))
		f.close()
		return line
	except IOError:
		pass


def archive_sources_patches(d,middle_name):
	'''archive sources and patches to tarball. middle_name will append strings ${middle_name} to ${PR} as middle name. for example, zlib-1.4.6-prepatch(middle_name).tar.gz '''
	import shutil
	verify_var(d)	
	if not_tarball(d):
		return
	
	source_tar_name = archive_sources(d,middle_name)
	if middle_name == "prepatch":
		if d.getVar('PATCHES_ARCHIVE_WITH_SERIES',True).upper() == 'TRUE':
			patch_tar_name = select_archive_patches(d,"all")
		elif d.getVar('PATCHES_ARCHIVE_WITH_SERIES',True).upper() == 'FALSE':
			patch_tar_name = select_archive_patches(d,"applying")
		else:
			bb.fatal("Please define 'PATCHES_ARCHIVE_WITH_SERIES' is strings 'True' or 'False' ")
	else:
		patch_tar_name = ''

	if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True).upper() not in 'SRPM':
		move_tarball_deploy(d,[source_tar_name,patch_tar_name])
	else:
		tarpackage = d.getVar('WORKDIR', True) + '/tar-package'
		if os.path.exists(tarpackage):
			os.remove(tarpackage)
		for package in source_tar_name, patch_tar_name:
			if package:
				store_package(d,str(package) + ' ')

def archive_scripts_logs(d):
	'''archive scripts and logs. scripts include .bb and .inc files and logs include stuff in "temp".'''

	work_dir = d.getVar('WORKDIR', True)
	os.chdir(work_dir)
	source_archive_log_with_scripts = d.getVar('SOURCE_ARCHIVE_LOG_WITH_SCRIPTS', True)
	if source_archive_log_with_scripts == 'logs_with_scripts':
		logdir = get_bb_inc(d)
		tarlog = archive_logs(d,logdir,True)
	elif source_archive_log_with_scripts == 'logs':
		if os.path.exists('temp'):
			tarlog = archive_logs(d,'temp',False)
	else:
		return
		
	if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True).upper() not in 'SRPM':
		move_tarball_deploy(d,[tarlog])

	else:
		store_package(d,tarlog)

def dumpdata(d):
	'''dump environment to "${P}-${PR}.showdata.dump" including all kinds of variables and functions when running a task'''
	workdir = bb.data.getVar('WORKDIR', d, 1)
	distro = bb.data.getVar('DISTRO', d, 1)
	s = d.getVar('S', True)
	pf = d.getVar('PF', True)
	target_sys = d.getVar('TARGET_SYS', True)
	licenses = get_licenses(d)
	dumpdir = d.getVar('DEPLOY_DIR', True) + '/sources/' + target_sys + '/' + licenses + '/' + pf
	if not os.path.exists(dumpdir):
		bb.mkdirhier(dumpdir)

	dumpfile = os.path.join(dumpdir, bb.data.expand("${P}-${PR}.showdata.dump",d))

	bb.note("Dumping metadata into '%s'" % dumpfile)
	f = open(dumpfile, "w")
	# emit variables and shell functions
        bb.data.emit_env(f, d, True)
	# emit the metadata which isnt valid shell
	for e in d.keys():
		if bb.data.getVarFlag(e, 'python', d):
			f.write("\npython %s () {\n%s}\n" % (e, bb.data.getVar(e, d, 1)))
	f.close()

def create_diff_gz(d):
	'''creating .diff.gz in ${DEPLOY_DIR_SRC}/${P}-${PR}.diff.g gz for mapping all content in 's' including patches to  xxx.diff.gz'''
	import shutil

	work_dir = d.getVar('WORKDIR', True)
	exclude_from = d.getVar('EXCLUDE_FROM', True).split()
	pf = d.getVar('PF', True)
	licenses = get_licenses(d)
	target_sys = d.getVar('TARGET_SYS', True)
	diff_dir = d.getVar('DEPLOY_DIR', True) + '/sources/' + target_sys + '/' + licenses + '/' + pf
	diff_file = os.path.join(diff_dir, bb.data.expand("${P}-${PR}.diff.gz",d))
	os.chdir(work_dir)
	f = open('temp/exclude-from-file', 'a')
	for i in exclude_from:
		f.write(i)
		f.write("\n")
	f.close()

	s=d.getVar('S', True)
	distro = d.getVar('DISTRO',True)
	dest = s + '/' + distro + '/files'
	if not os.path.exists(dest):
		bb.mkdirhier(dest)
	for i in os.listdir(os.getcwd()):
		if os.path.isfile(i):
			shutil.copy(i, dest)
	
	bb.note("Creating .diff.gz in ${DEPLOY_DIR_SRC}/${P}-${PR}.diff.gz")
	cmd = "LC_ALL=C TZ=UTC0 diff --exclude-from=" + work_dir + "/temp/exclude-from-file -Naur " + s + '.org' + ' ' +  s + " | gzip -c > " + diff_file
	d.setVar('DIFF', cmd + "\n")
	d.setVarFlag('DIFF', 'func', '1') 
	bb.build.exec_func('DIFF', d)
	shutil.rmtree(s + '.org', ignore_errors=True)

# This function will run when user want to get tarball for sources and patches after do_unpack
python do_archive_original_sources_patches(){
	archive_sources_patches(d,'prepatch')
}

# This function will run when user want to get tarball for patched sources after do_patch
python do_archive_patched_sources(){
	archive_sources_patches(d,'patched')
}

# This function will run when user want to get tarball for configured sources after do_configure
python do_archive_configured_sources(){
	archive_sources_patches(d,'configured')
}

# This function will run when user want to get tarball for logs or both logs and scripts(.bb and .inc files)
python do_archive_scripts_logs(){
	archive_scripts_logs(d)
}

# This function will run when user want to know what variable and functions in a running task are and also can get a diff file including 
# all content a package should include.
python do_dumpdata_create_diff_gz(){
	dumpdata(d)
	create_diff_gz(d)
}

# This functions prepare for archiving "linux-yocto" because this package create directory 's' before do_patch instead of after do_unpack.
# This is special control for archiving linux-yocto only.
python do_archive_linux_yocto(){
	s = d.getVar('S', True)
	if 'linux-yocto' in s:
		source_tar_name = archive_sources(d,'')
	if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True).upper() not in 'SRPM':
		move_tarball_deploy(d,[source_tar_name,''])
}
do_kernel_checkout[postfuncs] += "do_archive_linux_yocto "

# remove tarball for sources, patches and logs after creating srpm.
python do_remove_tarball(){
	if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True).upper() == 'SRPM':
		work_dir = d.getVar('WORKDIR', True)
		os.chdir(work_dir)
		try:
			for file in os.listdir(os.getcwd()):
				if file in get_package(d):
					os.remove(file)
			os.remove('tar-package')
		except (TypeError,OSError):
				pass
}
do_remove_taball[deptask] = "do_archive_scripts_logs"
do_package_write_rpm[postfuncs] += "do_remove_tarball "
export get_licenses
export get_package
