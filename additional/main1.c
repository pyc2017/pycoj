#include <Windows.h>
#include <process.h> 
#include "main.h"
#define FT2INT64(ft) \
	((long)((long)(ft).dwHighDateTime <<32 | (unsigned long long)(ft).dwLowDateTime))
unsigned int __stdcall mysolve(LPVOID lpParameter) {
	HANDLE *handle = NULL;
	handle = (HANDLE*)lpParameter;
	solve();
	CloseHandle(*handle);
	return 0;
}
int main() {
	FILETIME created,exited,kernel,user;HANDLE thread = NULL;thread =(HANDLE)_beginthreadex(NULL,0,mysolve,&thread,0,NULL);
	do {
		Sleep(100);
	} while (GetThreadTimes(thread, &created, &exited, &kernel, &user) && (FT2INT64(user) + FT2INT64(kernel))<(long)10000000);
	exit((FT2INT64(user) + FT2INT64(kernel))/10000);
}