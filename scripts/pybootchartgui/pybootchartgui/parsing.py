from __future__ import with_statement

import os
import string
import re
import tarfile
from collections import defaultdict

from samples import *
from process_tree import ProcessTree

class ParseError(Exception):
	"""Represents errors during parse of the bootchart."""
	def __init__(self, value):
            self.value = value

        def __str__(self):
            return self.value

def _parse_headers(file):
	"""Parses the headers of the bootchart."""
        def parse((headers,last), line): 
            if '=' in line: last,value = map(string.strip, line.split('=', 1))
            else:           value = line.strip()
            headers[last] += value
            return headers,last
        return reduce(parse, file.read().split('\n'), (defaultdict(str),''))[0]

def _parse_timed_blocks(file):
	"""Parses (ie., splits) a file into so-called timed-blocks. A
        timed-block consists of a timestamp on a line by itself followed
        by zero or more lines of data for that point in time."""
        def parse(block):
            lines = block.split('\n')
            if not lines:
                raise ParseError('expected a timed-block consisting a timestamp followed by data lines')
            try:
                return (int(lines[0]), lines[1:])
            except ValueError:
                raise ParseError("expected a timed-block, but timestamp '%s' is not an integer" % lines[0])
	blocks = file.read().split('\n\n')
        return [parse(block) for block in blocks if block.strip()]
	
def _parse_proc_ps_log(file):
	"""
	 * See proc(5) for details.
	 * 
	 * {pid, comm, state, ppid, pgrp, session, tty_nr, tpgid, flags, minflt, cminflt, majflt, cmajflt, utime, stime,
	 *  cutime, cstime, priority, nice, 0, itrealvalue, starttime, vsize, rss, rlim, startcode, endcode, startstack, 
	 *  kstkesp, kstkeip}
	"""
	processMap = {}
	ltime = 0
        timed_blocks = _parse_timed_blocks(file)
	for time, lines in timed_blocks:
		for line in lines:
			tokens = line.split(' ')

			offset = [index for index, token in enumerate(tokens[1:]) if token.endswith(')')][0]		
			pid, cmd, state, ppid = int(tokens[0]), ' '.join(tokens[1:2+offset]), tokens[2+offset], int(tokens[3+offset])
			userCpu, sysCpu, stime= int(tokens[13+offset]), int(tokens[14+offset]), int(tokens[21+offset])

			if processMap.has_key(pid):
				process = processMap[pid]
				process.cmd = cmd.replace('(', '').replace(')', '') # why rename after latest name??
			else:
				process = Process(pid, cmd, ppid, min(time, stime))
				processMap[pid] = process
			
			if process.last_user_cpu_time is not None and process.last_sys_cpu_time is not None and ltime is not None:
				userCpuLoad, sysCpuLoad = process.calc_load(userCpu, sysCpu, time - ltime)
				cpuSample = CPUSample('null', userCpuLoad, sysCpuLoad, 0.0)
				process.samples.append(ProcessSample(time, state, cpuSample))
			
			process.last_user_cpu_time = userCpu
			process.last_sys_cpu_time = sysCpu
		ltime = time

	startTime = timed_blocks[0][0]
	avgSampleLength = (ltime - startTime)/(len(timed_blocks)-1)	

	for process in processMap.values():
		process.set_parent(processMap)

	for process in processMap.values():
		process.calc_stats(avgSampleLength)
		
	return ProcessStats(processMap.values(), avgSampleLength, startTime, ltime)
	
def _parse_proc_stat_log(file):
	samples = []
	ltimes = None
	for time, lines in _parse_timed_blocks(file):
		# CPU times {user, nice, system, idle, io_wait, irq, softirq}		
		tokens = lines[0].split();
		times = [ int(token) for token in tokens[1:] ]
		if ltimes:
			user = float((times[0] + times[1]) - (ltimes[0] + ltimes[1]))
			system = float((times[2] + times[5] + times[6]) - (ltimes[2] + ltimes[5] + ltimes[6]))
			idle = float(times[3] - ltimes[3])
			iowait = float(times[4] - ltimes[4])
			
			aSum = max(user + system + idle + iowait, 1)
			samples.append( CPUSample(time, user/aSum, system/aSum, iowait/aSum) )
		
		ltimes = times		
		# skip the rest of statistics lines
	return samples

		
def _parse_proc_disk_stat_log(file, numCpu):
	"""
	Parse file for disk stats, but only look at the whole disks, eg. sda,
	not sda1, sda2 etc. The format of relevant lines should be:
	{major minor name rio rmerge rsect ruse wio wmerge wsect wuse running use aveq}
	"""
	DISK_REGEX = 'hd.$|sd.$'
	
	def is_relevant_line(line):
		return len(line.split()) == 14 and re.match(DISK_REGEX, line.split()[2])
	
	disk_stat_samples = []

	for time, lines in _parse_timed_blocks(file):
		sample = DiskStatSample(time)		
		relevant_tokens = [line.split() for line in lines if is_relevant_line(line)]
		
		for tokens in relevant_tokens:			
			disk, rsect, wsect, use = tokens[2], int(tokens[5]), int(tokens[9]), int(tokens[12])			
			sample.add_diskdata([rsect, wsect, use])
		
		disk_stat_samples.append(sample)
			
	disk_stats = []
	for sample1, sample2 in zip(disk_stat_samples[:-1], disk_stat_samples[1:]):
		interval = sample1.time - sample2.time
		sums = [ a - b for a, b in zip(sample1.diskdata, sample2.diskdata) ]
		readTput = sums[0] / 2.0 * 100.0 / interval
		writeTput = sums[1] / 2.0 * 100.0 / interval			
		util = float( sums[2] ) / 10 / interval / numCpu
		util = max(0.0, min(1.0, util))
		disk_stats.append(DiskSample(sample2.time, readTput, writeTput, util))
	
	return disk_stats
	
	
def get_num_cpus(headers):
    """Get the number of CPUs from the system.cpu header property. As the
    CPU utilization graphs are relative, the number of CPUs currently makes
    no difference."""
    if headers is None:
        return 1
    cpu_model = headers.get("system.cpu")
    if cpu_model is None:
        return 1
    mat = re.match(".*\\((\\d+)\\)", cpu_model)
    if mat is None:
        return 1
    return int(mat.group(1))

class ParserState:
    def __init__(self):
        self.headers = None
	self.disk_stats = None
	self.ps_stats = None
	self.cpu_stats = None

    def valid(self):
        return self.headers != None and self.disk_stats != None and self.ps_stats != None and self.cpu_stats != None


_relevant_files = set(["header", "proc_diskstats.log", "proc_ps.log", "proc_stat.log"])

def _do_parse(state, name, file):
    if name == "header":
        state.headers = _parse_headers(file)
    elif name == "proc_diskstats.log":
        state.disk_stats = _parse_proc_disk_stat_log(file, get_num_cpus(state.headers))
    elif name == "proc_ps.log":
        state.ps_stats = _parse_proc_ps_log(file)
    elif name == "proc_stat.log":
        state.cpu_stats = _parse_proc_stat_log(file)
    return state

def parse_file(state, filename):
    basename = os.path.basename(filename)
    if not(basename in _relevant_files):
        return state
    with open(filename, "rb") as file:
        return _do_parse(state, basename, file)

def parse_paths(state, paths):
    for path in paths:
        root,extension = os.path.splitext(path)
        if not(os.path.exists(path)):
            print "warning: path '%s' does not exist, ignoring." % path
            continue
        if os.path.isdir(path):
            files = [ f for f in [os.path.join(path, f) for f in os.listdir(path)] if os.path.isfile(f) ]
            files.sort()
            state = parse_paths(state, files)
        elif extension in [".tar", ".tgz", ".tar.gz"]:
            tf = None
            try:
                tf = tarfile.open(path, 'r:*')
                for name in tf.getnames():
                    state = _do_parse(state, name, tf.extractfile(name))
            except tarfile.ReadError, error:
                raise ParseError("error: could not read tarfile '%s': %s." % (path, error))
            finally:
                if tf != None:
                    tf.close()
        else:
            state = parse_file(state, path)
    return state

def parse(paths, prune):   
    state = parse_paths(ParserState(), paths)
    if not state.valid():
        raise ParseError("empty state: '%s' does not contain a valid bootchart" % ", ".join(paths))
    monitored_app = state.headers.get("profile.process")
    proc_tree = ProcessTree(state.ps_stats, monitored_app, prune)
    return (state.headers, state.cpu_stats, state.disk_stats, proc_tree)
